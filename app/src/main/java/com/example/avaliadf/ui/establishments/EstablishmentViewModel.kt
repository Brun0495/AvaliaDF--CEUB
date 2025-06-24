//package com.example.avaliadf.ui.establishments // Verifique o nome do seu pacote
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.avaliadf.data.model.Establishment
//import com.example.avaliadf.data.repository.EstablishmentRepository
//import com.example.avaliadf.data.util.ResultWrapper
//import kotlinx.coroutines.launch
//
//class EstablishmentListViewModel(
//    private val establishmentRepository: EstablishmentRepository
//) : ViewModel() {
//
//    private val _establishmentsList = MutableLiveData<List<Establishment>>()
//    val establishmentsList: LiveData<List<Establishment>> = _establishmentsList
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _errorMessage = MutableLiveData<String?>()
//    val errorMessage: LiveData<String?> = _errorMessage
//
//    // Mantém o título da tela dinâmico
//    private val _screenTitle = MutableLiveData<String>()
//    val screenTitle: LiveData<String> = _screenTitle
//
//    fun loadEstablishments(filterType: String, filterValue: String) {
//        // Define o título da tela com base no filtro
//        _screenTitle.value = filterValue.replaceFirstChar { it.uppercase() }
//
//        _isLoading.value = true
//        _errorMessage.value = null
//        viewModelScope.launch {
//            when (val result = establishmentRepository.getEstablishments(filterType, filterValue)) {
//                is ResultWrapper.Success<List<Establishment>> -> { // << CORREÇÃO AQUI
//                    if (result.data.isEmpty()) {
//                        _errorMessage.postValue("Nenhum local encontrado para este filtro.")
//                        _establishmentsList.postValue(emptyList())
//                    } else {
//                        _establishmentsList.postValue(result.data)
//                    }
//                }
//                is ResultWrapper.Error -> {
//                    _errorMessage.postValue(result.message)
//                    _establishmentsList.postValue(emptyList())
//                }
//            }
//            _isLoading.postValue(false)
//        }
//    }
//}