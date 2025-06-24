package com.example.avaliadf.ui.profile

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.avaliadf.R
import com.example.avaliadf.data.model.UserProfile
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.databinding.FragmentProfileBinding
import com.example.avaliadf.ui.base.BaseFragment // IMPORTAR

// 1. MUDAR A HERANÇA DA CLASSE
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    // 2. REMOVER _binding e binding
    // private var _binding: FragmentProfileBinding? = null
    // private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    // 3. REMOVER onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 4. CHAMAR super.onViewCreated() PRIMEIRO
        super.onViewCreated(view, savedInstanceState)

        // O resto da sua lógica continua intacta.
        val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
        val factory = ProfileViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        setupToolbar()
        setupMenu()
        setupClickListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbarProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit_profile -> {
                        viewModel.setEditMode(true)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupClickListeners() {
        binding.fabEditPhoto.setOnClickListener {
            Toast.makeText(context, "Funcionalidade para editar foto (a implementar).", Toast.LENGTH_SHORT).show()
        }

        binding.buttonSaveChanges.setOnClickListener {
            val newName = binding.editTextProfileName.text.toString()
            val newCpf = binding.editTextProfileCPF.text.toString()
            viewModel.saveChanges(newName, newCpf)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarProfile.isVisible = isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, "Erro: $error", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            profile?.let { bindProfileData(it) }
        }

        viewModel.isEditing.observe(viewLifecycleOwner) { isEditing ->
            toggleEditMode(isEditing)
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                viewModel.setEditMode(false)
            }
        }
    }

    private fun bindProfileData(profile: UserProfile) {
        binding.editTextProfileName.setText(profile.name)
        binding.editTextProfileEmail.setText(profile.email)
        binding.editTextProfileCPF.setText(profile.cpf ?: "")
        binding.textViewAvaliacoins.text = "${profile.avaliacoins} AvaliaCoins"

        Glide.with(this)
            .load(profile.photoUrl)
            .placeholder(R.drawable.ic_profile) // Usar um placeholder adequado
            .error(R.drawable.ic_profile)       // Usar um placeholder adequado
            .circleCrop()
            .into(binding.imageViewProfile)
    }

    private fun toggleEditMode(isEditing: Boolean) {
        binding.editTextProfileName.isEnabled = isEditing
        binding.editTextProfileCPF.isEnabled = isEditing
        binding.buttonSaveChanges.isVisible = isEditing
        binding.fabEditPhoto.isVisible = isEditing

        val context = requireContext()
        val inputBackgroundColor = if (isEditing) {
            androidx.core.content.ContextCompat.getColor(context, R.color.white)
        } else {
            // Usar uma cor que indique que o campo não é editável
            androidx.core.content.ContextCompat.getColor(context, R.color.black)
        }
        binding.editTextProfileName.setBackgroundColor(inputBackgroundColor)
        binding.editTextProfileCPF.setBackgroundColor(inputBackgroundColor)
    }

    // 5. REMOVER onDestroyView
}