// app/src/main/java/com/example/avaliadf/data/model/UserProfile.kt
package com.example.avaliadf.data.model

data class UserProfile(
    val uid: String,
    val name: String?,
    val email: String?,
    val cpf: String?,
    val photoUrl: String? = null // Para o futuro, quando tivermos fotos de perfil
)