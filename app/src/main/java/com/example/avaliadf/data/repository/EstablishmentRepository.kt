// app/src/main/java/com/example/avaliadf/data/repository/EstablishmentRepository.kt
package com.example.avaliadf.data.repository

import com.example.avaliadf.data.model.Establishment // << MUDOU PARA Establishment
import com.example.avaliadf.data.model.Review
import com.example.avaliadf.data.util.ResultWrapper

interface EstablishmentRepository {
    // A função agora aceita um tipo de filtro e um valor
    suspend fun getEstablishments(
        filterType: String, // "category" ou "city"
        filterValue: String
    ): ResultWrapper<List<Establishment>> // << MUDOU PARA Establishment

    // --- ADICIONE A NOVA FUNÇÃO ABAIXO ---
    suspend fun getEstablishmentById(id: String): ResultWrapper<Establishment?>

    suspend fun submitReview(review: Review): ResultWrapper<Boolean>
}