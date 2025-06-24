package com.example.avaliadf.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.util.ResultWrapper
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // LiveData para sinalizar que o e-mail foi enviado com sucesso
    private val _emailSent = MutableLiveData<Boolean>(false)
    val emailSent: LiveData<Boolean> = _emailSent

    fun sendPasswordResetEmail(email: String) {
        // Validação simples do formato do e-mail
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Por favor, insira um e-mail válido."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            when (repository.sendPasswordResetEmail(email)) {
                is ResultWrapper.Success -> {
                    _emailSent.postValue(true)
                }
                is ResultWrapper.Error -> {
                    _errorMessage.postValue("Ocorreu um erro. Tente novamente.")
                }
            }
            _isLoading.postValue(false)
        }
    }
}