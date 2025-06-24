// app/src/main/java/com/example/avaliadf/ui/profile/ProfileViewModel.kt
package com.example.avaliadf.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.model.UserProfile
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.util.ResultWrapper
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // --- NOVOS LIVEDATAS PARA CONTROLE DA EDIÇÃO ---
    private val _isEditing = MutableLiveData<Boolean>(false)
    val isEditing: LiveData<Boolean> = _isEditing

    private val _updateSuccess = MutableLiveData<Boolean>(false)
    val updateSuccess: LiveData<Boolean> = _updateSuccess
    // ---

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            when (val result = authRepository.getUserProfile()) {
                is ResultWrapper.Success -> {
                    _userProfile.postValue(result.data)
                }
                is ResultWrapper.Error -> {
                    _errorMessage.postValue(result.message)
                }
            }
            _isLoading.postValue(false)
        }
    }

    // --- NOVAS FUNÇÕES PARA O MODO DE EDIÇÃO ---

    /** Ativa ou desativa o modo de edição na tela. */
    fun setEditMode(isEditing: Boolean) {
        _isEditing.value = isEditing
    }

    /** Salva as alterações feitas no perfil do usuário. */
    fun saveChanges(newName: String, newCpf: String) {
        val currentProfile = _userProfile.value ?: return

        if (newName.isBlank()) {
            _errorMessage.value = "O nome não pode ficar em branco."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = authRepository.updateUserProfile(
                userId = currentProfile.uid,
                newName = newName,
                newCpf = newCpf.ifBlank { null } // Salva como nulo se o campo estiver vazio
            )

            when (result) {
                is ResultWrapper.Success -> {
                    _updateSuccess.postValue(true)
                    // Recarrega o perfil para mostrar os dados atualizados
                    loadUserProfile()
                    setEditMode(false) // Sai do modo de edição
                }
                is ResultWrapper.Error -> {
                    _errorMessage.postValue(result.message)
                }
            }
            _isLoading.postValue(false)
        }
    }
}