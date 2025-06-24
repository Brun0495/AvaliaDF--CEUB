package com.example.avaliadf.data.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    // Este campo guardará o ID do documento (ex: "asanorte")
    // @get:Exclude diz ao Firebase para não tentar salvar este campo de volta,
    // pois ele já é o ID do próprio documento.
    @get:Exclude var id: String = "",

    val name: String = "",
    val image: String? = null
) : Parcelable