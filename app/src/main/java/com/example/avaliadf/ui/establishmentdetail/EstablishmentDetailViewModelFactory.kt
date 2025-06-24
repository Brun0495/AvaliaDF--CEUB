// app/src/main/java/com/example/avaliadf/ui/establishmentdetail/EstablishmentDetailViewModelFactory.kt
package com.example.avaliadf.ui.establishmentdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avaliadf.data.repository.EstablishmentRepository

class EstablishmentDetailViewModelFactory(
    private val repository: EstablishmentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstablishmentDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstablishmentDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}