package com.example.avaliadf.ui.addestablishment

import androidx.lifecycle.*
import com.example.avaliadf.data.model.City
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.data.repository.CityRepository
import com.example.avaliadf.data.repository.EstablishmentRepository
import com.example.avaliadf.data.util.ResultWrapper
import kotlinx.coroutines.launch

class AddEstablishmentViewModel(
    private val establishmentRepository: EstablishmentRepository,
    private val cityRepository: CityRepository // Adicionamos o repositório de cidades
) : ViewModel() {

    // ... (isLoading, errorMessage, addSuccess LiveData como antes) ...
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _addSuccess = MutableLiveData<Boolean>(false)
    val addSuccess: LiveData<Boolean> = _addSuccess


    // Novo LiveData para a lista de cidades
    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            _cities.postValue(cityRepository.getCities())
        }
    }

    fun addEstablishment(
        name: String, category: String, cityId: String,
        address: String, phone: String, hours: String, imageUrl: String?,
        description: String?
    ) {
        if (name.isBlank() || category.isBlank() || cityId.isBlank() || address.isBlank()) {
            _errorMessage.value = "Por favor, preencha todos os campos obrigatórios."
            return
        }
        _isLoading.value = true
        _errorMessage.value = null

        val newEstablishment = Establishment(
            name = name, category = category.lowercase(), cityId = cityId,
            address = address, phone = phone, hours = hours, imageUrl = imageUrl,
            description = description,
            rating = 0.0, reviewCount = 0
        )

        viewModelScope.launch {
            when (val result = establishmentRepository.addEstablishment(newEstablishment)) {
                is ResultWrapper.Success -> _addSuccess.postValue(true)
                is ResultWrapper.Error -> _errorMessage.postValue(result.message)
            }
            _isLoading.postValue(false)
        }
    }
}