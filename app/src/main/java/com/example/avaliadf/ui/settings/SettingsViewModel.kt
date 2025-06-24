package com.example.avaliadf.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.util.ResultWrapper
import kotlinx.coroutines.launch

class SettingsViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // LiveData para sinalizar que a senha foi alterada com sucesso
    private val _passwordChanged = MutableLiveData<Boolean>(false)
    val passwordChanged: LiveData<Boolean> = _passwordChanged

    fun changePassword(currentPass: String, newPass: String, confirmPass: String) {
        // --- Validações Iniciais ---
        if (currentPass.isBlank() || newPass.isBlank() || confirmPass.isBlank()) {
            _errorMessage.value = "Todos os campos devem ser preenchidos."
            return
        }
        if (newPass.length < 6) {
            _errorMessage.value = "A nova senha deve ter no mínimo 6 caracteres."
            return
        }
        if (newPass != confirmPass) {
            _errorMessage.value = "As novas senhas não coincidem."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            when (val result = authRepository.changePassword(currentPass, newPass)) {
                is ResultWrapper.Success -> {
                    _passwordChanged.postValue(true)
                }
                is ResultWrapper.Error -> {
                    _errorMessage.postValue(result.message)
                }
            }
            _isLoading.postValue(false)
        }
    }
}