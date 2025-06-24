// app/src/main/java/com/example/avaliadf/ui/auth/AuthViewModelFactory.kt
package com.example.avaliadf.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avaliadf.data.repository.AuthRepository
// Remova a referência ao HomeViewModel e CityRepository daqui

class AuthViewModelFactory(
    private val repository: AuthRepository // << DEVE RECEBER APENAS O AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        // REMOVA A LÓGICA DO HomeViewModel DAQUI
        throw IllegalArgumentException("Unknown ViewModel class in AuthViewModelFactory: ${modelClass.name}")
    }
}