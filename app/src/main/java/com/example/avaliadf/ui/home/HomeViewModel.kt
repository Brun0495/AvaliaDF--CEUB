// app/src/main/java/com/example/avaliadf/ui/home/HomeViewModel.kt
package com.example.avaliadf.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.model.AuthResult
import com.example.avaliadf.data.model.City
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.CityRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val cityRepository: CityRepository
) : ViewModel() {

    // Este LiveData agora é nossa ÚNICA fonte da verdade sobre o status do usuário
    val authStatus: LiveData<AuthResult> = authRepository.authResultLiveData

    private val _citiesList = MutableLiveData<List<City>>()
    val citiesList: LiveData<List<City>> = _citiesList

    init {
        loadCities()
    }

    private fun loadCities() {
        _citiesList.value = cityRepository.getCities()
    }

    // A função de logout agora apenas chama o repositório.
    // Ela não precisa mais de um LiveData separado.
    fun logoutUser() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}