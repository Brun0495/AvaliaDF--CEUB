// app/src/main/java/com/example/avaliadf/data/repository/FirebaseAuthRepositoryImpl.kt
package com.example.avaliadf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.avaliadf.data.model.AuthResult
import com.example.avaliadf.data.model.UserProfile
import com.example.avaliadf.data.util.ResultWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
//import com.google.firebase.auth.FirebaseAuthTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException // << ADICIONE ESTA LINHA
import com.google.firebase.auth.FirebaseAuthWeakPasswordException   // << ADICIONE ESTA LINHA
import com.google.firebase.firestore.FieldValue                    // << ADICIONE ESTA LINHA
import com.google.firebase.firestore.FirebaseFirestore             // << ADICIONE ESTA LINHA
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance() // << ADICIONE ESTA LINHA (Instância do Firestore)

    private val _authResult = MutableLiveData<AuthResult>()
    override val authResultLiveData: LiveData<AuthResult> = _authResult

    override suspend fun signIn(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            _authResult.postValue(AuthResult(success = true, userId = firebaseAuth.currentUser?.uid))
        } catch (e: Exception) {
            val errorCode = when (e) {
                is FirebaseAuthInvalidUserException -> "auth/user-not-found"
                is FirebaseAuthInvalidCredentialsException -> "auth/wrong-password"
                //is FirebaseAuthTooManyRequestsException -> "auth/too-many-requests"
                else -> e.localizedMessage ?: "unknown_error"
            }
            _authResult.postValue(AuthResult(success = false, errorCode = errorCode, errorMessage = e.message))
        }
    }

    // NOVA FUNÇÃO PARA CRIAR USUÁRIO
    override suspend fun createUser(email: String, password: String) {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            _authResult.postValue(AuthResult(success = true, userId = result.user?.uid))
        } catch (e: Exception) {
            val errorCode = when (e) {
                is FirebaseAuthWeakPasswordException -> "auth/weak-password"
                is FirebaseAuthInvalidCredentialsException -> "auth/invalid-email" // Pode ser útil para formato de email
                is FirebaseAuthUserCollisionException -> "auth/email-already-in-use"
                else -> e.localizedMessage ?: "unknown_error_signup"
            }
            _authResult.postValue(AuthResult(success = false, errorCode = errorCode, errorMessage = e.message))
        }
    }

    // NOVA FUNÇÃO PARA SALVAR DETALHES DO USUÁRIO NO FIRESTORE
    override suspend fun saveUserDetails(userId: String, name: String, email: String): Boolean {
        val userProfile = hashMapOf(
            "name" to name,
            "email" to email,
            "avaliacoins" to 0, // Como no HTML
            "createdAt" to FieldValue.serverTimestamp() // Timestamp do servidor
        )
        return try {
            db.collection("users").document(userId).set(userProfile).await()
            true
        } catch (e: Exception) {
            // Idealmente, logar o erro aqui
            _authResult.postValue(AuthResult(success = false, errorCode = "firestore/user-save-failed", errorMessage = e.message))
            false
        }
    }

    // NOVA FUNÇÃO PARA SALVAR ESTABELECIMENTO NO FIRESTORE
    override suspend fun saveEstablishment(data: Map<String, Any>): Boolean {
        val establishmentData = HashMap(data) // Cria uma cópia mutável
        establishmentData["createdAt"] = FieldValue.serverTimestamp()

        return try {
            db.collection("establishments").add(establishmentData).await()
            // Para cadastro de estabelecimento, não postamos em _authResult diretamente,
            // pois não é uma operação de autenticação. O ViewModel tratará o resultado.
            true
        } catch (e: Exception) {
            // Idealmente, logar o erro aqui
            // O ViewModel pode expor um LiveData separado para o status do cadastro de estabelecimento
            false
        }
    }

    override suspend fun signOut() {
        try {
            firebaseAuth.signOut()
            // Postar um resultado de sucesso para logout, ou limpar o usuário
            // Para simplificar, o ViewModel pode apenas assumir sucesso e navegar.
            // Se precisar de um feedback específico do logout:
            // _authResult.postValue(AuthResult(success = true, event = AuthEvent.SIGNED_OUT))
            // Ou apenas limpar o usuário:
            _authResult.postValue(AuthResult(success = false, userId = null, errorCode = "SIGNED_OUT")) // Indica que não há mais usuário logado
        } catch (e: Exception) {
            // Erro durante o logout (raro, mas possível)
            _authResult.postValue(AuthResult(success = false, errorCode = "auth/signout-failed", errorMessage = e.message))
        }
    }

    // --- ADICIONE A NOVA FUNÇÃO ABAIXO ---
    override suspend fun getUserProfile(): ResultWrapper<UserProfile> {
        val firebaseUser = firebaseAuth.currentUser // Pega o usuário logado no Firebase

        return if (firebaseUser != null) {
            // Usuário está logado. Vamos retornar um perfil de exemplo (mock).
            // Usamos o email e uid reais, mas o nome e CPF são fixos para o protótipo.
            val mockUserProfile = UserProfile(
                uid = firebaseUser.uid,
                email = firebaseUser.email,
                name = "Usuário de Teste", // Nome fixo
                cpf = "***.123.456-**"      // CPF fixo
            )
            // Retorna o perfil de exemplo encapsulado em Sucesso
            ResultWrapper.Success(mockUserProfile)
        } else {
            // Nenhum usuário logado, retorna um erro.
            ResultWrapper.Error(message = "Nenhum usuário logado.")
        }
    }
}