package com.example.avaliadf.ui.establishments

import androidx.lifecycle.*
import com.example.avaliadf.data.model.City
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.data.repository.CityRepository
import com.example.avaliadf.data.repository.EstablishmentRepository
import com.example.avaliadf.data.util.ResultWrapper
import com.example.avaliadf.data.util.UIState
import kotlinx.coroutines.launch

class EstablishmentListViewModel(
    private val establishmentRepository: EstablishmentRepository,
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _screenTitle = MutableLiveData<String>()
    val screenTitle: LiveData<String> = _screenTitle

    private val _citiesList = MutableLiveData<List<City>>()
    val citiesList: LiveData<List<City>> = _citiesList

    // --- MUDANÇA PRINCIPAL: UM ÚNICO LIVEDATA PARA O ESTADO DA TELA ---
    private val _uiState = MutableLiveData<UIState<List<Establishment>>>()
    val uiState: LiveData<UIState<List<Establishment>>> = _uiState

    private var primaryFilterCategory: String? = null

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            _citiesList.postValue(cityRepository.getCities())
        }
    }

    fun initialLoad(filterType: String, filterValue: String) {
        if (filterType == "category") {
            primaryFilterCategory = filterValue
            _screenTitle.value = filterValue.replaceFirstChar { it.uppercase() }
            fetchEstablishments(category = primaryFilterCategory, cityId = null)
        } else { // filterType == "cityId"
            _screenTitle.value = "Estabelecimentos"
            fetchEstablishments(category = null, cityId = filterValue)
        }
    }

    fun onCityFilterChanged(cityId: String?) {
        fetchEstablishments(category = primaryFilterCategory, cityId = cityId)
    }

    private fun fetchEstablishments(category: String?, cityId: String?) {
        _uiState.value = UIState.Loading // 1. Emite o estado de Carregando
        viewModelScope.launch {
            when (val result = establishmentRepository.getEstablishments(category, cityId)) {
                is ResultWrapper.Success -> {
                    if (result.data.isEmpty()) {
                        _uiState.postValue(UIState.Empty) // 2. Emite o estado de Vazio
                    } else {
                        _uiState.postValue(UIState.Success(result.data)) // 3. Emite o estado de Sucesso
                    }
                }
                is ResultWrapper.Error -> {
                    _uiState.postValue(UIState.Error(result.message ?: "Erro desconhecido")) // 4. Emite o estado de Erro
                }
            }
        }
    }
}