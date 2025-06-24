// data/model/AuthStatus.kt
package com.example.avaliadf.data.model

sealed class AuthStatus {
    object Authenticated : AuthStatus()
    object Unauthenticated : AuthStatus()
    object Loading : AuthStatus() // Para indicar que estamos verificando
}