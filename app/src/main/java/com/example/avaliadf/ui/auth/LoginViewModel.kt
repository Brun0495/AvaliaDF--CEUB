// app/src/main/java/com/example/avaliadf/ui/auth/LoginViewModel.kt
package com.example.avaliadf.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData // << ADICIONE ESTE IMPORT
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.model.AuthResult
import com.example.avaliadf.data.repository.AuthRepository
// Remova FirebaseAuthRepositoryImpl se não for usado diretamente aqui e vier via construtor
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // --- ADICIONE ESTAS LINHAS para o estado de carregamento ---
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    // --- FIM DAS LINHAS ADICIONADAS ---

    // LiveData para o resultado da autenticação (vem do repositório)
    val authResultLiveData: LiveData<AuthResult> = authRepository.authResultLiveData

    // Função para iniciar o login
    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true // Agora _isLoading existe
            authRepository.signIn(email, pass)
            // O resultado será observado através de authResultLiveData.
            // O repositório postará o resultado, e o Fragment observará.
            // O _isLoading pode ser resetado no Fragment ao observar o resultado,
            // ou aqui se signIn no repositório fosse uma suspend fun que retorna o resultado.
            // Por simplicidade, vamos deixar o Fragment controlar o isLoading ao receber o resultado.
            // No entanto, é uma boa prática o ViewModel controlar seu próprio estado de loading.
            // A linha abaixo pode ser prematura se o signIn for rápido e o fragment não tiver tempo de reagir.
            // Vamos comentar por agora e deixar o Fragment controlar ao receber o AuthResult.
            // _isLoading.value = false
        }
    }
}