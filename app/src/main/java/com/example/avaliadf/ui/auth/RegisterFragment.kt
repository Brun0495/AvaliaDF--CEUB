package com.example.avaliadf.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.avaliadf.R
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.data.util.ResultWrapper
import com.example.avaliadf.databinding.FragmentRegisterBinding
import com.example.avaliadf.ui.base.BaseFragment // IMPORTAR

// 1. MUDAR A HERANÇA DA CLASSE
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    // 2. REMOVER _binding e binding
    // private var _binding: FragmentRegisterBinding? = null
    // private val binding get() = _binding!!

    private lateinit var viewModel: RegisterViewModel

    // 3. REMOVER onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 4. CHAMAR super.onViewCreated() PRIMEIRO
        super.onViewCreated(view, savedInstanceState)

        // O resto da sua lógica continua aqui, intacta.
        val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
        val factory = RegisterViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(context, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.registerUser(name, email, password)
        }

        binding.textViewBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.buttonRegister.isEnabled = !isLoading
        }

        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    Toast.makeText(context, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is ResultWrapper.Error -> {
                    Toast.makeText(context, "Erro: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // 5. REMOVER onDestroyView
}