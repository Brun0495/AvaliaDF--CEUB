// app/src/main/java/com/example/avaliadf/data/model/AuthResult.kt
package com.example.avaliadf.data.model

data class AuthResult(
    val success: Boolean,
    val userId: String? = null, // << ADICIONE ESTA LINHA
    val errorCode: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false // << ADICIONE ESTA LINHA (para resolver o erro no fragment)
    //    Consideraremos depois se é a melhor abordagem.
)