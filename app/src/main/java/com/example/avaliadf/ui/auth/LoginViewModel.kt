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
    fun signIn(email: String, password: String) {
        // Validação para campos em branco
        if (email.isBlank() || password.isBlank()) {
            // O repositório já lida com isso, mas é uma boa prática validar aqui também.
            return
        }

        // Inicia o processo e desativa o botão
        _isLoading.value = true

        viewModelScope.launch {
            try {
                authRepository.signIn(email, password)
                // Se o login for bem-sucedido, o observer do authResult no Fragment fará a navegação.
            } finally {
                // --- CORREÇÃO AQUI ---
                // O bloco 'finally' é executado SEMPRE, independentemente de sucesso ou erro.
                // Garantimos que o botão será reativado em qualquer cenário.
                _isLoading.postValue(false)
            }
        }
    }
}