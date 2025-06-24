package com.example.avaliadf.data.model

import com.google.firebase.Timestamp

data class Review(
    // O @JvmField é importante para o Firebase mapear o ID corretamente ao salvar.
    // E o var permite que a gente modifique o ID depois de criar o objeto.
    @JvmField var id: String = "",
    val establishmentId: String = "",
    val userId: String = "",
    val rating: Float = 0f,
    val comment: String? = null,
    val recommendationScore: Int = 0,
    val serviceRating: Int = 0,
    val returnChance: Int = 0,
    // O Timestamp do Firebase já é anulável por padrão
    val timestamp: Timestamp? = null
)