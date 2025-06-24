package com.example.avaliadf.data.repository
import com.example.avaliadf.data.model.City
class LocalCityRepositoryImpl : CityRepository {
    override fun getCities(): List<City> {
        return listOf(
            City(id = "aguasclaras", name = "Águas Claras", image = "aguas-claras.jpg"),
            City(id = "guara", name = "Guará", image = "guara.jpg"),
            City(id = "sobradinho", name = "Sobradinho", image = "sobradinho.jpg")
        )
    }
}