// app/src/main/java/com/example/avaliadf/ui/auth/RegisterViewModel.kt
package com.example.avaliadf.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avaliadf.data.model.AuthResult
import com.example.avaliadf.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Tipo de Cadastro (Usuário ou Estabelecimento)
    private val _registrationType = MutableLiveData<RegistrationType>(RegistrationType.USER)
    val registrationType: LiveData<RegistrationType> = _registrationType

    // Status do cadastro de Usuário (observa o LiveData do repositório)
    val userRegistrationAuthResult: LiveData<AuthResult> = authRepository.authResultLiveData

    // Status do cadastro de Estabelecimento
    private val _establishmentRegistrationStatus = MutableLiveData<EstablishmentRegistrationStatus>()
    val establishmentRegistrationStatus: LiveData<EstablishmentRegistrationStatus> = _establishmentRegistrationStatus

    // Estado de carregamento
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Erros de validação de formulário (poderia ser mais granular)
    private val _formError = MutableLiveData<String?>() // Mensagem de erro genérica para o formulário
    val formError: LiveData<String?> = _formError

    fun selectRegistrationType(type: RegistrationType) {
        _registrationType.value = type
        _formError.value = null // Limpa erros ao trocar de formulário
    }

    fun registerUser(name: String, email: String, pass: String, confirmPass: String) {
        if (name.isBlank()) {
            _formError.value = "Nome não pode estar em branco."
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _formError.value = "Formato de e-mail inválido."
            return
        }
        if (pass.length < 6) {
            _formError.value = "Senha deve ter pelo menos 6 caracteres."
            return
        }
        if (pass != confirmPass) {
            _formError.value = "As senhas não coincidem."
            return
        }
        _formError.value = null // Limpa erro anterior
        _isLoading.value = true

        viewModelScope.launch {
            authRepository.createUser(email, pass)
            // O resultado será observado através de userRegistrationAuthResult
            // Precisamos de uma forma de saber que o authResultLiveData foi atualizado
            // especificamente por esta chamada. Isso pode ser um desafio se o LiveData
            // for muito genérico. Idealmente, createUser retornaria o AuthResult.

            // Assumindo que userRegistrationAuthResult será atualizado,
            // o Fragment irá observar e então chamar saveUserDetails se for sucesso.
            // Vamos simplificar por agora e o Fragment chamará saveUserDetails.
            // Esta é uma limitação do _authResult ser genérico no repositório.
            // Por enquanto, o Fragment fará a chamada para saveUserDetails após
            // observar um AuthResult de sucesso com um userId.
            _isLoading.value = false // O Fragment controlará o loading para a segunda etapa (saveUserDetails)
        }
    }

    fun saveUserDetailsAfterAuth(userId: String, name: String, email: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val success = authRepository.saveUserDetails(userId, name, email)
            if (!success) {
                // Se saveUserDetails postar no _authResult do repositório com erro,
                // o userRegistrationAuthResult no fragment será atualizado.
                // Se não, precisamos de um LiveData específico ou um callback.
                // Na implementação atual do repositório, ele posta um AuthResult de erro.
            }
            // O fragment observará userRegistrationAuthResult para o resultado final.
            _isLoading.value = false
        }
    }

    fun registerEstablishment(
        name: String,
        isBranchSelectedValue: String, // "Sim", "Não", ou "Selecione"
        address: String,
        uf: String,
        neighborhood: String,
        complement: String?,
        phone: String,
        cnpj: String,
        contactEmail: String
    ) {
        // Validações
        if (name.isBlank()) {
            _formError.value = "Nome do estabelecimento não pode estar em branco."
            return
        }
        if (isBranchSelectedValue == "Selecione") {
            _formError.value = "Selecione se é filial do app."
            return
        }
        if (address.isBlank()) {
            _formError.value = "Endereço não pode estar em branco."
            return
        }
        if (uf.isBlank() || uf.length != 2) {
            _formError.value = "UF inválida (deve ter 2 caracteres)."
            return
        }
        if (neighborhood.isBlank()) {
            _formError.value = "Bairro não pode estar em branco."
            return
        }
        if (phone.isBlank()) { // Validação de telefone pode ser mais robusta
            _formError.value = "Telefone não pode estar em branco."
            return
        }
        if (cnpj.isBlank()) { // Validação de CNPJ pode ser mais robusta
            _formError.value = "CNPJ não pode estar em branco."
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches()) {
            _formError.value = "Email de contato inválido."
            return
        }
        _formError.value = null
        _isLoading.value = true

        val establishmentData = mapOf(
            "name" to name,
            "isBranchApp" to (isBranchSelectedValue == "Sim"),
            "address" to address,
            "uf" to uf.uppercase(),
            "neighborhood" to neighborhood,
            "complement" to (complement ?: ""),
            "phone" to phone,
            "cnpj" to cnpj,
            "contactEmail" to contactEmail
            // "createdAt" será adicionado pelo repositório
        )

        viewModelScope.launch {
            val success = authRepository.saveEstablishment(establishmentData)
            if (success) {
                _establishmentRegistrationStatus.postValue(EstablishmentRegistrationStatus(success = true))
            } else {
                _establishmentRegistrationStatus.postValue(
                    EstablishmentRegistrationStatus(
                        success = false,
                        message = "Falha ao salvar dados do estabelecimento.",
                        errorType = EstablishmentErrorType.FIRESTORE_ERROR
                    )
                )
            }
            _isLoading.value = false
        }
    }

    // Chamado quando o ViewModel não é mais necessário (ex: Fragment é destruído)
    override fun onCleared() {
        super.onCleared()
        _formError.value = null // Limpa para evitar memory leaks com observers
    }
}