// app/src/main/java/com/example/avaliadf/data/repository/LocalEstablishmentRepositoryImpl.kt
package com.example.avaliadf.data.repository

import com.example.avaliadf.data.model.Establishment
import android.util.Log // << --- IMPORTANTE: ADICIONE ESTE IMPORT
import com.example.avaliadf.data.model.Review
import com.example.avaliadf.data.util.ResultWrapper

// Esta é a nossa implementação "fake" ou "mock" para o protótipo
class LocalEstablishmentRepositoryImpl : EstablishmentRepository {

    // Aqui criamos uma lista fixa de dados de exemplo, como se viesse do Firestore
    private val mockEstablishments = listOf(
        Establishment(
            id = "1",
            name = "Restaurante A (Sobradinho)",
            category = "restaurante",
            city = "sobradinho",
            imageUrl = "restaurante1.jpg", // Nome do arquivo em assets/TrabProjetoIntegrador/img/
            hours = "11:00 - 23:00",
            phone = "61988887777",
            rating = 4.8,
            mapLink = "http://maps.google.com"
        ),
        Establishment(
            id = "2",
            name = "Hotel B (Águas Claras)",
            category = "hotel",
            city = "aguasclaras",
            imageUrl = "hotel1.jpg",
            hours = "Check-in 14:00",
            phone = "61977776666",
            rating = 4.5,
            mapLink = "http://maps.google.com"
        ),
        Establishment(
            id = "3",
            name = "Parque C (Guará)",
            category = "lazer",
            city = "guara",
            imageUrl = "lazer1.jpg",
            hours = "08:00 - 18:00",
            phone = "N/A",
            rating = 4.9,
            mapLink = "http://maps.google.com"
        ),
        Establishment(
            id = "4",
            name = "Restaurante D (Águas Claras)",
            category = "restaurante",
            city = "aguasclaras",
            imageUrl = "restaurante2.jpg",
            hours = "12:00 - 22:00",
            phone = "61966665555",
            rating = 4.6,
            mapLink = "http://maps.google.com"
        )
        // Adicione mais dados de exemplo conforme necessário para seus testes
    )

    override suspend fun getEstablishments(
        filterType: String,
        filterValue: String
    ): ResultWrapper<List<Establishment>> {
        // Filtramos a nossa lista local em vez de fazer uma query no Firestore
        val filteredList = mockEstablishments.filter { establishment ->
            when (filterType) {
                "category" -> establishment.category.equals(filterValue, ignoreCase = true)
                "city" -> establishment.city.equals(filterValue, ignoreCase = true)
                else -> false // Se o tipo de filtro for desconhecido, não retorna nada
            }
        }
        // Retornamos a lista filtrada, sempre com sucesso, pois é local.
        return ResultWrapper.Success(filteredList)
    }

    // --- ADICIONE A NOVA FUNÇÃO ABAIXO ---
    override suspend fun getEstablishmentById(id: String): ResultWrapper<Establishment?> {
        // Procura na lista mockada pelo primeiro item que tem o ID correspondente
        val establishment = mockEstablishments.find { it.id == id }

        return if (establishment != null) {
            // Se encontrou, retorna com Sucesso
            ResultWrapper.Success(establishment)
        } else {
            // Se não encontrou, retorna um Erro
            ResultWrapper.Error(message = "Estabelecimento não encontrado.")
        }
    }

    override suspend fun submitReview(review: Review): ResultWrapper<Boolean> {
        // Para o protótipo, não vamos salvar em lugar nenhum.
        // Apenas imprimimos no Logcat para confirmar que a função foi chamada
        // e retornamos sucesso.
        Log.d("LocalRepository", "--- AVALIAÇÃO RECEBIDA ---")
        Log.d("LocalRepository", "Estabelecimento ID: ${review.establishmentId}")
        Log.d("LocalRepository", "Usuário ID: ${review.userId}")
        Log.d("LocalRepository", "Nota: ${review.rating}")
        Log.d("LocalRepository", "Comentário: ${review.comment}")
        Log.d("LocalRepository", "--------------------------")

        // Simula que a operação foi bem-sucedida.
        return ResultWrapper.Success(true)
    }
}