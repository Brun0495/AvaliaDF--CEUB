package com.example.avaliadf.ui.establishments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avaliadf.data.repository.CityRepository
import com.example.avaliadf.data.repository.EstablishmentRepository

class EstablishmentListViewModelFactory(
    private val establishmentRepository: EstablishmentRepository,
    private val cityRepository: CityRepository // Adicionamos a dependência que faltava
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstablishmentListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Passamos os dois repositórios para o ViewModel
            return EstablishmentListViewModel(establishmentRepository, cityRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}