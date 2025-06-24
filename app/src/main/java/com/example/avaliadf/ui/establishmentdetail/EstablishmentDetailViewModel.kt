// app/src/main/java/com/example/avaliadf/ui/establishmentdetail/EstablishmentDetailViewModel.kt
package com.example.avaliadf.ui.establishmentdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.data.repository.EstablishmentRepository
import com.example.avaliadf.data.util.ResultWrapper
import kotlinx.coroutines.launch

class EstablishmentDetailViewModel(
    private val repository: EstablishmentRepository
) : ViewModel() {

    private val _establishment = MutableLiveData<Establishment?>()
    val establishment: LiveData<Establishment?> = _establishment

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadEstablishmentDetails(id: String) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            when (val result = repository.getEstablishmentById(id)) {
                is ResultWrapper.Success -> {
                    _establishment.postValue(result.data)
                    if (result.data == null) {
                        _errorMessage.postValue("Estabelecimento não encontrado.")
                    }
                }
                is ResultWrapper.Error -> {
                    _errorMessage.postValue(result.message)
                }
            }
            _isLoading.postValue(false)
        }
    }
}