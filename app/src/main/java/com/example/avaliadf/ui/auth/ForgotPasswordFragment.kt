package com.example.avaliadf.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.databinding.FragmentForgotPasswordBinding
import com.example.avaliadf.ui.base.BaseFragment // IMPORTAR

// 1. MUDAR A HERANÇA DA CLASSE
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {

    // 2. REMOVER _binding e binding
    // private var _binding: FragmentForgotPasswordBinding? = null
    // private val binding get() = _binding!!

    private lateinit var viewModel: ForgotPasswordViewModel

    // 3. REMOVER onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 4. CHAMAR super.onViewCreated() PRIMEIRO
        super.onViewCreated(view, savedInstanceState)

        // O resto da sua lógica continua intacta.
        val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
        val factory = ForgotPasswordViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory)[ForgotPasswordViewModel::class.java]

        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.buttonSendLink.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            viewModel.sendPasswordResetEmail(email)
        }

        binding.textViewBackToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.buttonSendLink.isEnabled = !isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, "Erro: $error", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.emailSent.observe(viewLifecycleOwner) { sent ->
            if (sent) {
                Toast.makeText(context, "Link de recuperação enviado para o seu e-mail.", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
    }

    // 5. REMOVER onDestroyView
}