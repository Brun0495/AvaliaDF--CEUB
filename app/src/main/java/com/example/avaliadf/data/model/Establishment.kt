// app/src/main/java/com/example/avaliadf/data/model/Restaurante.kt
package com.example.avaliadf.data.model

// Usaremos @Parcelize para poder passar o objeto entre fragments, se necessário.
// Adicione o plugin 'kotlin-parcelize' no seu build.gradle se ainda não tiver.
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Establishment(
    val id: String = "",
    val name: String = "",
    val imageUrl: String? = null,
    val hours: String? = null,
    val phone: String? = null,
    val rating: Double? = 0.0, // Usar Double para a nota
    val mapLink: String? = null,
    val city: String = "", // Usado para o filtro
    val category: String = "" // Para garantir que estamos pegando a categoria certa
) : Parcelable