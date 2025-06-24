// app/src/main/java/com/example/avaliadf/ui/review/ReviewViewModelFactory.kt
package com.example.avaliadf.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.EstablishmentRepository

class ReviewViewModelFactory(
    private val establishmentRepository: EstablishmentRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(establishmentRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}