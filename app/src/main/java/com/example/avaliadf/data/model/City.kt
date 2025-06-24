// app/src/main/java/com/example/avaliadf/data/model/City.kt
package com.example.avaliadf.data.model

data class City(
    val id: String = "", // Para o ID do documento do Firestore
    val name: String = "",
    val image: String? = null, // URL da imagem da cidade
    val htmlPage: String? = null // Do HTML original, podemos adaptar para navegação nativa depois
    // Futuramente, podemos adicionar um campo como: val navigationDestinationId: Int? = null
)