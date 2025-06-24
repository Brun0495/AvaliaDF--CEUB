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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val establishmentRepository: EstablishmentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _establishment = MutableLiveData<Establishment?>()
    val establishment: LiveData<Establishment?> = _establishment

    private val _reviewSubmissionState = MutableLiveData<ResultWrapper<Boolean>>()
    val reviewSubmissionState: LiveData<ResultWrapper<Boolean>> = _reviewSubmissionState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Novo LiveData para controlar a etapa atual do formulário
    private val _currentStep = MutableLiveData(1)
    val currentStep: LiveData<Int> = _currentStep

    fun loadEstablishmentDetails(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = establishmentRepository.getEstablishmentById(id)) {
                is ResultWrapper.Success -> _establishment.postValue(result.data)
                is ResultWrapper.Error -> _establishment.postValue(null)
            }
            _isLoading.postValue(false)
        }
    }

    fun nextStep() {
        _currentStep.value = (_currentStep.value ?: 1) + 1
    }

    fun submitFullReview(
        establishmentId: String,
        rating: Float,
        comment: String,
        recommendationScore: Int,
        serviceRating: Int,
        returnChance: Int
    ) {
        _isLoading.value = true
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            _reviewSubmissionState.value = ResultWrapper.Error(message = "Usuário não autenticado.")
            _isLoading.value = false
            return
        }

        val review = Review(
            establishmentId = establishmentId,
            userId = userId,
            rating = rating,
            comment = comment.ifBlank { null },
            recommendationScore = recommendationScore,
            serviceRating = serviceRating,
            returnChance = returnChance,
            timestamp = Timestamp.now()
        )

        viewModelScope.launch {
            when (establishmentRepository.submitReview(review)) {
                is ResultWrapper.Success -> {
                    // Se a avaliação foi salva com sucesso, adiciona os AvaliaCoins
                    val coinsResult = authRepository.addAvaliaCoins(userId, 50)
                    _reviewSubmissionState.postValue(coinsResult) // O resultado final depende do sucesso em adicionar moedas
                }
                is ResultWrapper.Error -> {
                    // Se falhou ao salvar a avaliação, informa o erro
                    _reviewSubmissionState.postValue(ResultWrapper.Error(message = "Falha ao enviar avaliação."))
                }
            }
            _isLoading.postValue(false)
        }
    }
}