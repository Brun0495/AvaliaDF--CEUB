
package com.example.avaliadf.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Establishment(
    val id: String = "",
    val name: String = "",
    val imageUrl: String? = null,
    val hours: String? = null,
    val phone: String? = null,
    val rating: Double? = 0.0,
    val mapLink: String? = null,

    // --- CAMPOS CORRIGIDOS E ADICIONADOS ---
    val city: String = "", // Este campo deve corresponder ao 'city' em seu Firebase
    val cityId: String = "", // Adicionado para corresponder ao 'cityId' em seu Firebase
    val category: String = "",
    val address: String = "" ,

    val reviewCount: Int = 0,

    val description: String? = null

) : Parcelable