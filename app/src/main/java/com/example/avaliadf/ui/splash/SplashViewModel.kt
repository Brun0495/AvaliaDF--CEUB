// ui/splash/SplashViewModel.kt
package com.example.avaliadf.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.model.AuthStatus // Importe o AuthStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _authStatus = MutableLiveData<AuthStatus>(AuthStatus.Loading)
    val authStatus: LiveData<AuthStatus> = _authStatus

    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        checkUserAuthentication()
    }

    private fun checkUserAuthentication() {
        viewModelScope.launch {
            delay(1500) // Simula um tempo de carregamento/splash, ajuste conforme necessário
            if (firebaseAuth.currentUser != null) {
                _authStatus.value = AuthStatus.Authenticated
            } else {
                _authStatus.value = AuthStatus.Unauthenticated
            }
        }
    }
}