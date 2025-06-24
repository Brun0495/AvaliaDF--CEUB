// app/src/main/java/com/example/avaliadf/ui/auth/RegisterStateModels.kt
// (Você pode criar este arquivo ou colocar as classes dentro do RegisterViewModel se preferir,
// mas separá-las pode ser mais organizado se crescerem)
package com.example.avaliadf.ui.auth

import com.example.avaliadf.data.model.AuthResult

// Enum para o tipo de cadastro
enum class RegistrationType {
    USER,
    ESTABLISHMENT
}

// Classe para o resultado do cadastro de estabelecimento
data class EstablishmentRegistrationStatus(
    val success: Boolean,
    val message: String? = null,
    val errorType: EstablishmentErrorType? = null // Opcional, para erros específicos
)

enum class EstablishmentErrorType {
    VALIDATION_ERROR,
    FIRESTORE_ERROR
}

// Classe para representar eventos de validação
// Usaremos uma classe simples para mensagens de erro por enquanto
// Poderia ser uma sealed class para erros mais específicos por campo
data class FormValidationError(val fieldId: String, val message: String)