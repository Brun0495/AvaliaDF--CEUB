package com.example.avaliadf.ui.home
import androidx.lifecycle.*
import com.example.avaliadf.data.repository.*
class HomeViewModelFactory(
    private val authRepository: AuthRepository,
    private val cityRepository: CityRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(authRepository, cityRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}