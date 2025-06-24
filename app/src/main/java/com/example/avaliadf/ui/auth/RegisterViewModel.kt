package com.example.avaliadf.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.util.ResultWrapper
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResult = MutableLiveData<ResultWrapper<FirebaseUser>>()
    val registerResult: LiveData<ResultWrapper<FirebaseUser>> = _registerResult

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = authRepository.registerUser(name, email, password)
            _registerResult.postValue(result)
            _isLoading.postValue(false)
        }
    }
}