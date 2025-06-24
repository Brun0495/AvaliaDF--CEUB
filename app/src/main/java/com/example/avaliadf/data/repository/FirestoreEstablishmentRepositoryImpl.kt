//// app/src/main/java/com/example/avaliadf/data/repository/FirestoreEstablishmentRepositoryImpl.kt
//package com.example.avaliadf.data.repository
//
//import com.example.avaliadf.data.model.Establishment // << MUDOU
//import com.example.avaliadf.data.util.ResultWrapper
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//import kotlinx.coroutines.tasks.await
//
//class FirestoreEstablishmentRepositoryImpl : EstablishmentRepository { // << MUDOU
//
//    private val placesCollection = FirebaseFirestore.getInstance().collection("places")
//
//    override suspend fun getEstablishments(
//        filterType: String,
//        filterValue: String
//    ): ResultWrapper<List<Establishment>> { // << MUDOU
//        return try {
//            // Query base na coleção "places"
//            val query: Query = placesCollection.whereEqualTo(filterType, filterValue)
//
//            val querySnapshot = query.get().await()
//
//            val establishments = querySnapshot.documents.mapNotNull { doc ->
//                // Mapeia para nosso novo objeto Establishment
//                doc.toObject(Establishment::class.java)?.copy(id = doc.id)
//            }
//
//            ResultWrapper.Success(establishments)
//        } catch (e: Exception) {
//            ResultWrapper.Error(e, "Erro ao carregar estabelecimentos: ${e.message}")
//        }
//    }
//}