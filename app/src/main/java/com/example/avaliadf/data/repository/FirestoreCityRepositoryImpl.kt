//// app/src/main/java/com/example/avaliadf/data/repository/FirestoreCityRepositoryImpl.kt
//package com.example.avaliadf.data.repository
//
//import com.example.avaliadf.data.model.City
//import com.example.avaliadf.data.util.ResultWrapper
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//import kotlinx.coroutines.tasks.await
//
//class FirestoreCityRepositoryImpl : CityRepository {
//
//    private val db = FirebaseFirestore.getInstance()
//
//    override suspend fun getCities(): ResultWrapper<List<City>> {
//        return try {
//            val querySnapshot = db.collection("cities")
//                .orderBy("name", Query.Direction.ASCENDING) // Ordena por nome, como no HTML
//                .get()
//                .await()
//
//            val cities = querySnapshot.documents.mapNotNull { document ->
//                // Mapeia o documento para o objeto City, tratando campos que podem ser nulos
//                City(
//                    id = document.id,
//                    name = document.getString("name") ?: "",
//                    image = document.getString("image"),
//                    htmlPage = document.getString("htmlPage")
//                )
//            }
//            ResultWrapper.Success(cities)
//        } catch (e: Exception) {
//            // Log.e("FirestoreCityRepo", "Error fetching cities", e)
//            ResultWrapper.Error(e, "Erro ao buscar cidades: ${e.message}")
//        }
//    }
//}