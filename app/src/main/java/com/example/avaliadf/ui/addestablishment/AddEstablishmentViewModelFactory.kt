package com.example.avaliadf.ui.addestablishment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avaliadf.data.repository.CityRepository
import com.example.avaliadf.data.repository.EstablishmentRepository

class AddEstablishmentViewModelFactory(
    private val establishmentRepository: EstablishmentRepository,
    private val cityRepository: CityRepository // Adicionamos aqui
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEstablishmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEstablishmentViewModel(establishmentRepository, cityRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}