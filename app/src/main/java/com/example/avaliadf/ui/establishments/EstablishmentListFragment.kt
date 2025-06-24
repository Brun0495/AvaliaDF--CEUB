package com.example.avaliadf.ui.establishments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.avaliadf.data.repository.LocalEstablishmentRepositoryImpl
import com.example.avaliadf.databinding.FragmentEstablishmentListBinding
//import com.example.avaliadf.ui.establishmentdetail.EstablishmentDetailFragmentDirections.Companion.actionEstablishmentListFragmentToReviewFragment
import com.example.avaliadf.ui.establishments.adapters.EstablishmentListAdapter

class EstablishmentListFragment : Fragment() {
    private val args: EstablishmentListFragmentArgs by navArgs()
    private var _binding: FragmentEstablishmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EstablishmentListViewModel
    private lateinit var establishmentAdapter: EstablishmentListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEstablishmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = LocalEstablishmentRepositoryImpl()
        val factory = EstablishmentListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[EstablishmentListViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupObservers()

        // Inicia o carregamento com os argumentos recebidos da navegação
        viewModel.loadEstablishments(args.filterType, args.filterValue)
    }

    private fun setupToolbar() {
        binding.toolbarRestaurantes.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    // Dentro de EstablishmentListFragment.kt
    private fun setupRecyclerView() {
        establishmentAdapter = EstablishmentListAdapter(
            onItemClick = { establishment ->
                // Ação de clique para o item inteiro: navegar para os detalhes
                val action = EstablishmentListFragmentDirections
                    .actionEstablishmentListFragmentToEstablishmentDetailFragment(establishment.id)
                findNavController().navigate(action)
            },
            onLigarClick = { telefone ->
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telefone")))
            },
            onRotaClick = { mapLink ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mapLink)))
            },
            onAvaliarClick = { establishment ->
                // Navega para a tela de avaliação, passando o ID do estabelecimento
                val action = EstablishmentListFragmentDirections
                    .actionEstablishmentListFragmentToReviewFragment(establishment.id)
                findNavController().navigate(action)
            }
        )
        binding.recyclerViewRestaurantes.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewRestaurantes.adapter = establishmentAdapter
    }

    private fun setupObservers() {
        viewModel.screenTitle.observe(viewLifecycleOwner) { binding.toolbarRestaurantes.title = it }
        viewModel.isLoading.observe(viewLifecycleOwner) { binding.progressBar.isVisible = it }
        viewModel.establishmentsList.observe(viewLifecycleOwner) {
            binding.recyclerViewRestaurantes.isVisible = it.isNotEmpty()
            establishmentAdapter.submitList(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            binding.textViewError.isVisible = error != null
            binding.textViewError.text = error
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}