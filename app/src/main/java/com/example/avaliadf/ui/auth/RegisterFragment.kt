// app/src/main/java/com/example/avaliadf/ui/auth/RegisterFragment.kt
package com.example.avaliadf.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.avaliadf.R
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.databinding.FragmentRegisterBinding // Importe seu ViewBinding gerado

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterViewModel

    // Variável para armazenar temporariamente o nome e email do usuário para salvar no Firestore
    // após a criação bem-sucedida no Auth.
    private var tempUserName: String? = null
    private var tempUserEmail: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // Configurar o Spinner para "É filial do app?"
        // O array R.array.is_branch_options foi definido no strings.xml anteriormente
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.is_branch_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerIsBranch.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar ViewModel
        // Em um app real, use injeção de dependência (Hilt, Koin)
        // --- INICIALIZAÇÃO DO VIEWMODEL COM FACTORY CORRETA ---
        val repository = FirebaseAuthRepositoryImpl()
        val factory = AuthViewModelFactory(repository) // << AGORA SÓ PRECISA DO AuthRepository
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.radioGroupRegisterType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonUser -> viewModel.selectRegistrationType(RegistrationType.USER)
                R.id.radioButtonEstablishment -> viewModel.selectRegistrationType(RegistrationType.ESTABLISHMENT)
            }
        }

        binding.buttonRegisterUser.setOnClickListener {
            val name = binding.editTextUserName.text.toString().trim()
            val email = binding.editTextUserEmail.text.toString().trim()
            val password = binding.editTextUserPassword.text.toString()
            val confirmPassword = binding.editTextUserConfirmPassword.text.toString()

            // Armazena temporariamente para usar após o sucesso do Auth
            tempUserName = name
            tempUserEmail = email

            viewModel.registerUser(name, email, password, confirmPassword)
        }

        binding.buttonRegisterEstablishment.setOnClickListener {
            val name = binding.editTextEstablishmentName.text.toString().trim()
            val isBranch = binding.spinnerIsBranch.selectedItem.toString() // "Sim", "Não", "Selecione"
            val address = binding.editTextEstablishmentAddress.text.toString().trim()
            val uf = binding.editTextEstablishmentUF.text.toString().trim().uppercase()
            val neighborhood = binding.editTextEstablishmentNeighborhood.text.toString().trim()
            val complement = binding.editTextEstablishmentComplement.text.toString().trim()
            val phone = binding.editTextEstablishmentPhone.text.toString().trim()
            val cnpj = binding.editTextEstablishmentCNPJ.text.toString().trim()
            val contactEmail = binding.editTextEstablishmentContactEmail.text.toString().trim()

            viewModel.registerEstablishment(
                name, isBranch, address, uf, neighborhood, complement.ifEmpty { null }, phone, cnpj, contactEmail
            )
        }

        binding.textViewBackToLogin.setOnClickListener {
            findNavController().popBackStack()
            // Ou, para garantir que volte ao LoginFragment limpando este da pilha:
            // findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun setupObservers() {
        viewModel.registrationType.observe(viewLifecycleOwner) { type ->
            binding.layoutFormUser.isVisible = type == RegistrationType.USER
            binding.layoutFormEstablishment.isVisible = type == RegistrationType.ESTABLISHMENT
            // Limpar campos de erro dos TextInputLayouts se desejar
            binding.tilUserName.error = null
            binding.tilUserEmail.error = null
            // ... etc para outros campos
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarRegister.isVisible = isLoading
            binding.buttonRegisterUser.isEnabled = !isLoading
            binding.buttonRegisterEstablishment.isEnabled = !isLoading
            binding.radioGroupRegisterType.isEnabled = !isLoading // Pode ser útil desabilitar
        }

        viewModel.formError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                // Aqui você poderia direcionar o erro para o TextInputLayout específico se tivesse
                // um LiveData mais granular para erros por campo. Ex:
                // if (it.contains("Nome")) binding.tilUserName.error = it
            }
        }

        viewModel.userRegistrationAuthResult.observe(viewLifecycleOwner) { authResult ->
            // Este observer será ativado após createUser E após saveUserDetails (se saveUserDetails postar no mesmo LiveData)
            if (viewModel.isLoading.value == false) { // Se o ViewModel já parou de carregar
                binding.progressBarRegister.isVisible = false
            }

            if (authResult.success && authResult.userId != null) {
                if (tempUserName != null && tempUserEmail != null && authResult.errorCode == null && !authResult.isLoading) { // Sucesso inicial do Auth e não é um resultado de loading
                    val userId = authResult.userId // Agora deve funcionar
                    val name = tempUserName ?: ""
                    val email = tempUserEmail ?: ""

                    tempUserName = null
                    tempUserEmail = null

                    viewModel.saveUserDetailsAfterAuth(userId, name, email)
                } else if (authResult.errorCode == null && !authResult.isLoading) { // Sucesso final (após saveUserDetails) e não é um resultado de loading
                    Toast.makeText(context, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }

            } else if (!authResult.success && authResult.errorCode != null) {
                val friendlyMessage = when (authResult.errorCode) {
                    "auth/email-already-in-use" -> "Este e-mail já está em uso."
                    "auth/weak-password" -> "Senha muito fraca. Tente uma mais forte."
                    "auth/invalid-email" -> "Formato de e-mail inválido."
                    "firestore/user-save-failed" -> "Usuário criado, mas falha ao salvar detalhes: ${authResult.errorMessage}"
                    else -> authResult.errorMessage ?: "Erro desconhecido no cadastro."
                }
                Toast.makeText(context, friendlyMessage, Toast.LENGTH_LONG).show()
                tempUserName = null // Limpa para evitar retentativas incorretas
                tempUserEmail = null
            }
        }


        viewModel.establishmentRegistrationStatus.observe(viewLifecycleOwner) { status ->
            if (status.success) {
                Toast.makeText(context, status.message ?: "Estabelecimento cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                Toast.makeText(context, status.message ?: "Erro ao cadastrar estabelecimento.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tempUserName = null // Limpa as variáveis temporárias
        tempUserEmail = null
    }
}