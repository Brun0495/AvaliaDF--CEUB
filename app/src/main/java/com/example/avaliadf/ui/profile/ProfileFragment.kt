// app/src/main/java/com/example/avaliadf/ui/profile/ProfileFragment.kt
package com.example.avaliadf.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.avaliadf.R
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
        val factory = ProfileViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        setupToolbar()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbarProfile.setNavigationOnClickListener {
            // Ação para o botão de voltar na toolbar
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarProfile.isVisible = isLoading
            binding.cardProfile.isVisible = !isLoading // Esconde o card enquanto carrega
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            val hasError = errorMessage != null
            binding.textViewProfileError.isVisible = hasError
            if(hasError) {
                binding.textViewProfileError.text = errorMessage
            }
        }

        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            // Preenche os campos da UI com os dados do perfil
            binding.textViewProfileName.text = profile.name ?: "Nome não disponível"
            binding.textViewProfileEmail.text = profile.email ?: "E-mail não disponível"
            binding.textViewProfileCPF.text = "CPF: ${profile.cpf ?: "Não disponível"}"

            // Carrega a imagem de perfil (se houver uma URL) ou mostra o placeholder
            Glide.with(this)
                .load(profile.photoUrl) // Irá carregar a URL da foto no futuro
                .placeholder(R.drawable.ic_user_profile_placeholder) // O placeholder que já criamos
                .error(R.drawable.ic_user_profile_placeholder) // Imagem de erro
                .circleCrop() // Deixa a imagem redonda
                .into(binding.imageViewProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}