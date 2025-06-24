// app/src/main/java/com/example/avaliadf/data/model/Review.kt
package com.example.avaliadf.data.model

// Representa uma avaliação que será salva
data class Review(
    val id: String = "", // O ID do próprio documento de avaliação
    val establishmentId: String,
    val userId: String,
    val rating: Float, // A nota em estrelas (ex: 4.5f)
    val comment: String?,
    val timestamp: Long = System.currentTimeMillis() // A data/hora da avaliação
)