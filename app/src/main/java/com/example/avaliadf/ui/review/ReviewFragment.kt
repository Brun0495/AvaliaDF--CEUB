// app/src/main/java/com/example/avaliadf/ui/review/ReviewFragment.kt
package com.example.avaliadf.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.EstablishmentRepository
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.data.repository.LocalEstablishmentRepositoryImpl
import com.example.avaliadf.data.util.ResultWrapper
import com.example.avaliadf.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReviewViewModel
    private val args: ReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Para o protótipo, usamos os repositórios locais e de autenticação real
        val establishmentRepository: EstablishmentRepository = LocalEstablishmentRepositoryImpl()
        val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
        val factory = ReviewViewModelFactory(establishmentRepository, authRepository)
        viewModel = ViewModelProvider(this, factory)[ReviewViewModel::class.java]

        setupToolbar()
        setupClickListeners()
        setupObservers()

        // Pede ao ViewModel para carregar os detalhes usando o ID recebido da navegação
        viewModel.loadEstablishmentDetails(args.establishmentId)
    }

    private fun setupToolbar() {
        binding.toolbarReview.setNavigationOnClickListener {
            findNavController().navigateUp() // Ação de voltar
        }
    }

    private fun setupClickListeners() {
        binding.buttonSubmitReview.setOnClickListener {
            val rating = binding.ratingBar.rating
            val comment = binding.editTextComment.text.toString()
            viewModel.submitReview(args.establishmentId, rating, comment)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarReview.isVisible = isLoading
            binding.buttonSubmitReview.isEnabled = !isLoading
        }

        // Observa o estabelecimento carregado para mostrar o nome
        viewModel.establishment.observe(viewLifecycleOwner) { establishment ->
            if (establishment != null) {
                binding.textViewEstablishmentName.text = establishment.name
            } else {
                binding.textViewEstablishmentName.text = "Local não encontrado"
            }
        }

        // Observa o resultado do envio da avaliação
        viewModel.reviewSubmissionState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    Toast.makeText(context, "Obrigado pela sua avaliação!", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack() // Volta para a tela anterior
                }
                is ResultWrapper.Error -> {
                    Toast.makeText(context, "Erro: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}