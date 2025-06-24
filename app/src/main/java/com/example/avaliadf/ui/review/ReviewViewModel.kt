// app/src/main/java/com/example/avaliadf/ui/review/ReviewViewModel.kt
package com.example.avaliadf.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.data.model.Review
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.EstablishmentRepository
import com.example.avaliadf.data.util.ResultWrapper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val establishmentRepository: EstablishmentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // Para guardar os detalhes do estabelecimento que está sendo avaliado
    private val _establishment = MutableLiveData<Establishment?>()
    val establishment: LiveData<Establishment?> = _establishment

    // Para controlar o estado de envio da avaliação
    private val _reviewSubmissionState = MutableLiveData<ResultWrapper<Boolean>>()
    val reviewSubmissionState: LiveData<ResultWrapper<Boolean>> = _reviewSubmissionState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Carrega os dados de um estabelecimento para sabermos o nome dele.
     */
    fun loadEstablishmentDetails(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = establishmentRepository.getEstablishmentById(id)) {
                is ResultWrapper.Success -> _establishment.postValue(result.data)
                is ResultWrapper.Error -> _establishment.postValue(null) // Lida com erro
            }
            _isLoading.postValue(false)
        }
    }

    /**
     * Envia a avaliação do usuário.
     */
    fun submitReview(establishmentId: String, rating: Float, comment: String) {
        _isLoading.value = true
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            _reviewSubmissionState.value = ResultWrapper.Error(message = "Usuário não autenticado.")
            _isLoading.value = false
            return
        }

        if (rating == 0f) {
            _reviewSubmissionState.value = ResultWrapper.Error(message = "Por favor, selecione uma nota.")
            _isLoading.value = false
            return
        }

        val review = Review(
            establishmentId = establishmentId,
            userId = userId,
            rating = rating,
            comment = comment.ifBlank { null }
        )

        viewModelScope.launch {
            val result = establishmentRepository.submitReview(review)
            _reviewSubmissionState.postValue(result)
            _isLoading.postValue(false)
        }
    }
}