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
}