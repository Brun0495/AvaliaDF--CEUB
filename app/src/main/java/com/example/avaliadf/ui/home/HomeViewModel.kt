// app/src/main/java/com/example/avaliadf/ui/home/HomeViewModel.kt
package com.example.avaliadf.ui.home

import androidx.lifecycle.*
import com.example.avaliadf.data.model.AuthResult
import com.example.avaliadf.data.model.City
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.CityRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val cityRepository: CityRepository
) : ViewModel() {

    val authStatus: LiveData<AuthResult> = authRepository.authResultLiveData
    private val _citiesList = MutableLiveData<List<City>>()
    val citiesList: LiveData<List<City>> = _citiesList

    init {
        loadCities()
    }

    // A chamada para o repositório agora está dentro de um viewModelScope.launch
    private fun loadCities() {
        viewModelScope.launch {
            _citiesList.postValue(cityRepository.getCities())
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}