package com.example.avaliadf.ui.review

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.avaliadf.R
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.EstablishmentRepository
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.data.repository.FirestoreEstablishmentRepositoryImpl
import com.example.avaliadf.data.util.ResultWrapper
import com.example.avaliadf.databinding.FragmentReviewBinding
import com.example.avaliadf.ui.base.BaseFragment // IMPORTAR

// 1. MUDAR A HERANÇA DA CLASSE
class ReviewFragment : BaseFragment<FragmentReviewBinding>(FragmentReviewBinding::inflate) {

    // 2. REMOVER _binding e binding
    // private var _binding: FragmentReviewBinding? = null
    // private val binding get() = _binding!!

    private lateinit var viewModel: ReviewViewModel
    private val args: ReviewFragmentArgs by navArgs()

    // Variáveis para guardar as respostas de cada etapa
    private var generalRating: Float = 0f
    private var comment: String = ""
    private var recommendationScore: Int = -1
    private var serviceRating: Int = -1
    private var returnChance: Int = -1

    // 3. REMOVER onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 4. CHAMAR super.onViewCreated() PRIMEIRO
        super.onViewCreated(view, savedInstanceState)

        // O resto da sua lógica continua intacta.
        val establishmentRepository: EstablishmentRepository = FirestoreEstablishmentRepositoryImpl()
        val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
        val factory = ReviewViewModelFactory(establishmentRepository, authRepository)
        viewModel = ViewModelProvider(this, factory)[ReviewViewModel::class.java]

        setupToolbar()
        setupSpinners()
        setupClickListeners()
        setupObservers()

        viewModel.loadEstablishmentDetails(args.establishmentId)
    }

    private fun setupToolbar() {
        binding.toolbarReview.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSpinners() {
        binding.spinnerRecommendation.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, (0..10).map { it.toString() })
        binding.spinnerService.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, (1..10).map { it.toString() })
        binding.spinnerReturnChance.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, (1..5).map { it.toString() })
    }

    private fun setupClickListeners() {
        binding.buttonNextSubmit.setOnClickListener {
            handleNextSubmitClick()
        }
        binding.buttonBackToHome.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    private fun handleNextSubmitClick() {
        val currentStep = viewModel.currentStep.value ?: 1
        if (!validateStep(currentStep)) return

        saveStepData(currentStep)

        if (currentStep < 4) {
            viewModel.nextStep()
        } else {
            viewModel.submitFullReview(
                establishmentId = args.establishmentId,
                rating = generalRating,
                comment = comment,
                recommendationScore = recommendationScore,
                serviceRating = serviceRating,
                returnChance = returnChance
            )
        }
    }

    private fun validateStep(step: Int): Boolean {
        return when (step) {
            1 -> {
                if (binding.ratingBarGeneral.rating == 0f) {
                    Toast.makeText(context, "Por favor, selecione uma nota.", Toast.LENGTH_SHORT).show()
                    false
                } else true
            }
            else -> true // Simplificado para as outras etapas
        }
    }

    private fun saveStepData(step: Int) {
        when (step) {
            1 -> {
                generalRating = binding.ratingBarGeneral.rating
                comment = binding.editTextComment.text.toString()
            }
            2 -> recommendationScore = binding.spinnerRecommendation.selectedItem.toString().toInt()
            3 -> serviceRating = binding.spinnerService.selectedItem.toString().toInt()
            4 -> returnChance = binding.spinnerReturnChance.selectedItem.toString().toInt()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarReview.isVisible = isLoading
        }

        viewModel.currentStep.observe(viewLifecycleOwner) { step ->
            binding.step1.isVisible = step == 1
            binding.step2.isVisible = step == 2
            binding.step3.isVisible = step == 3
            binding.step4.isVisible = step == 4

            binding.buttonNextSubmit.text = if (step == 4) "Enviar Avaliação" else "Próxima"
        }

        viewModel.establishment.observe(viewLifecycleOwner) { establishment ->
            binding.textViewEstablishmentName.text = "Avaliar: ${establishment?.name ?: "Local"}"
        }

        viewModel.reviewSubmissionState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    binding.formContainer.isVisible = false
                    binding.thankYouContainer.isVisible = true
                }
                is ResultWrapper.Error -> {
                    Toast.makeText(context, "Erro: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // 5. REMOVER onDestroyView
}