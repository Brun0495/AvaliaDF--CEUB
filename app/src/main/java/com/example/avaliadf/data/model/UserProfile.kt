// app/src/main/java/com/example/avaliadf/data/model/UserProfile.kt
package com.example.avaliadf.data.model

import com.google.firebase.firestore.Exclude

// Adicionar um construtor vazio é uma boa prática para o Firestore
data class UserProfile(
    var name: String? = null,
    var email: String? = null,
    var cpf: String? = null, // Agora é opcional (pode ser nulo)
    var photoUrl: String? = null,
    var avaliacoins: Long = 0 // Adicionamos para corresponder ao Firebase
) {
    // Exclui o campo 'uid' de ser salvo no documento, pois já é o ID do documento
    @get:Exclude var uid: String = ""
}