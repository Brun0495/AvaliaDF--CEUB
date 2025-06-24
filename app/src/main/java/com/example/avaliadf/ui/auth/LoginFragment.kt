package com.example.avaliadf.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.avaliadf.R
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.databinding.FragmentLoginBinding
import com.example.avaliadf.ui.base.BaseFragment // IMPORTAR O BASE FRAGMENT

// 1. MUDAR A HERANÇA DA CLASSE
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    // 2. REMOVER _binding e binding - Eles agora vêm do BaseFragment
    // private var _binding: FragmentLoginbing? = null
    // private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    // 3. REMOVER o método onCreateView - O BaseFragment cuida disso

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 4. CHAMAR super.onViewCreated() PRIMEIRO. É ISTO QUE APLICA A CORREÇÃO DA BARRA DE STATUS
        super.onViewCreated(view, savedInstanceState)

        // O resto do seu código permanece exatamente igual!
        val repository = FirebaseAuthRepositoryImpl()
        // Assumindo que sua Factory chama-se AuthViewModelFactory
        val factory = AuthViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.buttonAccess.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Preencha e-mail e senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginViewModel.signIn(email, password)
        }

        binding.textViewCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.textViewForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.buttonLoginGoogle.setOnClickListener {
            Toast.makeText(context, "Login com Google desativado no momento (MVP)", Toast.LENGTH_LONG).show()
        }

        loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarLogin.isVisible = isLoading
            binding.buttonAccess.isEnabled = !isLoading
        }

        loginViewModel.authResultLiveData.observe(viewLifecycleOwner) { result ->
            if (result.success) {
                Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                val errorMessage = when (result.errorCode) {
                    "auth/user-not-found" -> "Nenhum usuário encontrado com este e-mail."
                    "auth/wrong-password" -> "Senha incorreta."
                    "auth/invalid-email" -> "E-mail inválido."
                    "auth/too-many-requests" -> "Muitas tentativas de login. Tente novamente mais tarde."
                    else -> result.errorMessage ?: "Erro ao fazer login."
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    // 5. REMOVER o método onDestroyView - O BaseFragment cuida disso
}