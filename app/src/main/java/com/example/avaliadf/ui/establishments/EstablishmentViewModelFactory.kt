package com.example.avaliadf.ui.establishments

import androidx.lifecycle.*
import com.example.avaliadf.data.repository.EstablishmentRepository

class EstablishmentListViewModelFactory(
    private val repository: EstablishmentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstablishmentListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstablishmentListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}