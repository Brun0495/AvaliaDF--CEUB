// app/src/main/java/com/example/avaliadf/data/repository/AuthRepository.kt
package com.example.avaliadf.data.repository

import androidx.lifecycle.LiveData
import com.example.avaliadf.data.model.AuthResult
import com.example.avaliadf.data.model.UserProfile
import com.example.avaliadf.data.util.ResultWrapper

interface AuthRepository {
    val authResultLiveData: LiveData<AuthResult>
    suspend fun signIn(email: String, password: String)
    suspend fun createUser(email: String, password: String) // << ADICIONE ESTA LINHA
    suspend fun signOut() // << ADICIONE ESTA LINHA
    // Adicione aqui futuras funções como signOut, getCurrentUser, etc.

    // Funções para salvar dados adicionais no Firestore (poderiam estar em outro repositório)
    suspend fun saveUserDetails(userId: String, name: String, email: String): Boolean
    suspend fun saveEstablishment(data: Map<String, Any>): Boolean // << ADICIONE ESTA LINHA

    // --- ADICIONE A NOVA FUNÇÃO ABAIXO ---
    suspend fun getUserProfile(): ResultWrapper<UserProfile>
}