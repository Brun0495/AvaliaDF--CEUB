// app/src/main/java/com/example/avaliadf/ui/establishmentdetail/EstablishmentDetailFragment.kt
package com.example.avaliadf.ui.establishmentdetail

import android.content.Intent
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.example.avaliadf.R
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.data.repository.LocalEstablishmentRepositoryImpl
import com.example.avaliadf.databinding.FragmentEstablishmentDetailBinding

class EstablishmentDetailFragment : Fragment() {

    private var _binding: FragmentEstablishmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EstablishmentDetailViewModel
    private val args: EstablishmentDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstablishmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = LocalEstablishmentRepositoryImpl()
        val factory = EstablishmentDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[EstablishmentDetailViewModel::class.java]

        setupToolbar()
        setupObservers()

        // Pede ao ViewModel para carregar os detalhes usando o ID recebido da navegação
        viewModel.loadEstablishmentDetails(args.establishmentId)
    }

    private fun setupToolbar() {
        binding.toolbarDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
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
    }

    private fun bindEstablishmentDetails(establishment: Establishment) {
        // Preenche a UI com os dados recebidos
        binding.collapsingToolbar.title = establishment.name
        binding.textViewDetailHours.text = "Horário: ${establishment.hours ?: "Não informado"}"
        binding.textViewDetailPhone.text = "Telefone: ${establishment.phone ?: "Não informado"}"
        binding.textViewDetailRating.text = establishment.rating?.toString() ?: "N/A"

        // Carrega a imagem do header
        val assetPath = "file:///android_asset/TrabProjetoIntegrador/img/${establishment.imageUrl}"
        Glide.with(this)
            .load(assetPath)
            .placeholder(R.drawable.ic_city_placeholder)
            .error(R.drawable.ic_city_placeholder)
            .into(binding.imageViewDetailHeader)

        // Configura os botões de ação
        binding.buttonDetailLigar.setOnClickListener {
            establishment.phone?.let { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))) }
        }
        binding.buttonDetailVerRota.setOnClickListener {
            establishment.mapLink?.let { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
        }
        binding.buttonDetailAvaliar.setOnClickListener {
            Toast.makeText(context, "Navegar para avaliar ${establishment.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}