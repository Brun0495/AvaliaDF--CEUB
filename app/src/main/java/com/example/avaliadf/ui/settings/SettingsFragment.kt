package com.example.avaliadf.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.databinding.FragmentSettingsBinding
import com.example.avaliadf.ui.base.BaseFragment // IMPORTAR

// 1. MUDAR A HERANÇA DA CLASSE
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    // 2. REMOVER _binding e binding
    // private var _binding: FragmentSettingsBinding? = null
    // private val binding get() = _binding!!

    private lateinit var viewModel: SettingsViewModel

    // 3. REMOVER onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 4. CHAMAR super.onViewCreated() PRIMEIRO
        super.onViewCreated(view, savedInstanceState)

        // O resto da sua lógica continua aqui.
        val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
        val factory = SettingsViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        setupToolbar()
        setupClickListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbarSettings.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupClickListeners() {
        binding.buttonChangePassword.setOnClickListener {
            val currentPassword = binding.editTextCurrentPassword.text.toString()
            val newPassword = binding.editTextNewPassword.text.toString()
            val confirmPassword = binding.editTextConfirmNewPassword.text.toString()

            viewModel.changePassword(currentPassword, newPassword, confirmPassword)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarSettings.isVisible = isLoading
            binding.buttonChangePassword.isEnabled = !isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.passwordChanged.observe(viewLifecycleOwner) { hasChanged ->
            if (hasChanged) {
                Toast.makeText(context, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    // 5. REMOVER onDestroyView
}