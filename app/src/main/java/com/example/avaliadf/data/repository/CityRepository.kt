// app/src/main/java/com/example/avaliadf/data/repository/CityRepository.kt
package com.example.avaliadf.data.repository

import com.example.avaliadf.data.model.City
import com.example.avaliadf.data.util.ResultWrapper

interface CityRepository {
    fun getCities(): List<City>
}