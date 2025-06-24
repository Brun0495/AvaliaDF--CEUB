package com.example.avaliadf.ui.establishmentdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.avaliadf.R
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.data.repository.FirestoreEstablishmentRepositoryImpl
import com.example.avaliadf.databinding.FragmentEstablishmentDetailBinding
import com.example.avaliadf.ui.base.BaseFragment // IMPORTAR
import com.example.avaliadf.ui.establishmentdetail.adapters.ReviewAdapter

// 1. MUDAR A HERANÇA DA CLASSE
class EstablishmentDetailFragment :
    BaseFragment<FragmentEstablishmentDetailBinding>(FragmentEstablishmentDetailBinding::inflate) {

    // 2. REMOVER _binding e binding
    // private var _binding: FragmentEstablishmentDetailBinding? = null
    // private val binding get() = _binding!!

    private lateinit var viewModel: EstablishmentDetailViewModel
    private val args: EstablishmentDetailFragmentArgs by navArgs()

    private lateinit var reviewAdapter: ReviewAdapter

    // 3. REMOVER onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 4. CHAMAR super.onViewCreated() PRIMEIRO
        super.onViewCreated(view, savedInstanceState)

        // O resto da sua lógica continua intacta.
        val repository = FirestoreEstablishmentRepositoryImpl()
        val factory = EstablishmentDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[EstablishmentDetailViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupObservers()

        viewModel.loadEstablishmentDetails(args.establishmentId)
    }

    private fun setupToolbar() {
        binding.toolbarDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.recyclerViewReviews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewAdapter
            isNestedScrollingEnabled = false // Mantém a rolagem suave dentro da CollapsingToolbarLayout
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingContainer.isVisible = isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.establishment.observe(viewLifecycleOwner) { establishment ->
            establishment?.let { bindEstablishmentDetails(it) }
        }

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            binding.recyclerViewReviews.isVisible = reviews.isNotEmpty()
            binding.textViewNoReviews.isVisible = reviews.isEmpty()
            reviewAdapter.submitList(reviews)
        }

        viewModel.averageRating.observe(viewLifecycleOwner) { avgRating ->
            val formattedRating = String.format("%.1f", avgRating)
            binding.textViewDetailRating.text = formattedRating
        }
    }

    private fun bindEstablishmentDetails(establishment: Establishment) {
        binding.collapsingToolbar.title = establishment.name
        binding.textViewDetailHours.text = "Horário: ${establishment.hours ?: "Não informado"}"
        binding.textViewDetailPhone.text = "Telefone: ${establishment.phone ?: "Não informado"}"

        binding.textViewDetailDescription.text = establishment.description ?: "Nenhuma descrição disponível."

        // Assumindo que a imagem está nos assets
        val assetPath = "file:///android_asset/TrabProjetoIntegrador/img/${establishment.imageUrl}"
        Glide.with(this)
            .load(assetPath)
            .placeholder(R.drawable.ic_city_placeholder) // Imagem genérica enquanto carrega
            .error(R.drawable.ic_city_placeholder) // Imagem genérica em caso de erro
            .into(binding.imageViewDetailHeader)

        binding.buttonDetailLigar.setOnClickListener {
            establishment.phone?.let { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))) }
        }
        binding.buttonDetailVerRota.setOnClickListener {
            establishment.mapLink?.let { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
        }
        binding.buttonDetailAvaliar.setOnClickListener {
            val action = EstablishmentDetailFragmentDirections
                .actionEstablishmentDetailFragmentToReviewFragment(establishment.id)
            findNavController().navigate(action)
        }
    }

    // 5. REMOVER onDestroyView
}