package com.example.avaliadf.ui.establishmentdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.data.model.Review // << ADICIONE IMPORT
import com.example.avaliadf.data.repository.EstablishmentRepository
import com.example.avaliadf.data.util.ResultWrapper
import kotlinx.coroutines.launch

class EstablishmentDetailViewModel(
    private val repository: EstablishmentRepository
) : ViewModel() {

    private val _establishment = MutableLiveData<Establishment?>()
    val establishment: LiveData<Establishment?> = _establishment

    // --- NOVOS LIVEDATAS PARA AS AVALIAÇÕES ---
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private val _averageRating = MutableLiveData<Float>()
    val averageRating: LiveData<Float> = _averageRating
    // ---

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadEstablishmentDetails(id: String) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            // Busca os detalhes do estabelecimento
            when (val result = repository.getEstablishmentById(id)) {
                is ResultWrapper.Success -> _establishment.postValue(result.data)
                is ResultWrapper.Error -> _errorMessage.postValue(result.message)
            }

            // Busca a lista de avaliações para este estabelecimento
            when (val reviewsResult = repository.getReviewsForEstablishment(id)) {
                is ResultWrapper.Success -> {
                    val reviewList = reviewsResult.data
                    _reviews.postValue(reviewList)
                    calculateAverageRating(reviewList) // Calcula a média
                }
                is ResultWrapper.Error -> {
                    // Não define um erro aqui, pois a tela pode funcionar sem as avaliações
                    _reviews.postValue(emptyList())
                }
            }

            _isLoading.postValue(false)
        }
    }

    // Função para calcular a média das notas
    private fun calculateAverageRating(reviews: List<Review>) {
        if (reviews.isEmpty()) {
            _averageRating.value = 0f
            return
        }
        val totalRating = reviews.sumOf { it.rating.toDouble() }
        _averageRating.value = (totalRating / reviews.size).toFloat()
    }
}