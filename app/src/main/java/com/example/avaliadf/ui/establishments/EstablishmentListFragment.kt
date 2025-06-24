package com.example.avaliadf.ui.establishments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.avaliadf.R
import com.example.avaliadf.data.model.City
import com.example.avaliadf.data.repository.*
import com.example.avaliadf.data.util.UIState
import com.example.avaliadf.databinding.FragmentEstablishmentListBinding
import com.example.avaliadf.ui.establishments.adapters.EstablishmentListAdapter
import com.example.avaliadf.ui.base.BaseFragment

class EstablishmentListFragment : BaseFragment<FragmentEstablishmentListBinding>(FragmentEstablishmentListBinding::inflate) {

    private val args: EstablishmentListFragmentArgs by navArgs()
    //private var _binding: FragmentEstablishmentListBinding? = null
    //private val binding get() = _binding!!

    private lateinit var viewModel: EstablishmentListViewModel
    private lateinit var establishmentAdapter: EstablishmentListAdapter
    private var citiesList: List<City> = emptyList()

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        _binding = FragmentEstablishmentListBinding.inflate(inflater, container, false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val establishmentRepository: EstablishmentRepository = FirestoreEstablishmentRepositoryImpl()
        val cityRepository: CityRepository = FirestoreCityRepositoryImpl()
        val factory = EstablishmentListViewModelFactory(establishmentRepository, cityRepository)
        viewModel = ViewModelProvider(this, factory)[EstablishmentListViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupObservers()

        viewModel.initialLoad(args.filterType, args.filterValue)
    }

    private fun setupToolbar() {
        binding.toolbarRestaurantes.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupRecyclerView() {
        establishmentAdapter = EstablishmentListAdapter(
            onItemClick = { establishment ->
                val action = EstablishmentListFragmentDirections
                    .actionEstablishmentListFragmentToEstablishmentDetailFragment(establishment.id)
                findNavController().navigate(action)
            },
            onLigarClick = { telefone -> startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telefone"))) },
            onRotaClick = { mapLink -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mapLink))) }
        )
        binding.recyclerViewRestaurantes.adapter = establishmentAdapter
        binding.recyclerViewRestaurantes.layoutManager = LinearLayoutManager(context)
    }

    private fun setupClickListeners() {
        binding.buttonAddEstablishment.setOnClickListener {
            // Navega para a tela de adicionar estabelecimento
            findNavController().navigate(R.id.action_establishmentListFragment_to_addEstablishmentFragment)
        }
    }

    private fun setupObservers() {
        viewModel.screenTitle.observe(viewLifecycleOwner) { title -> binding.toolbarRestaurantes.title = title }

        // --- OBSERVER ÚNICO E SIMPLIFICADO ---
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.recyclerViewRestaurantes.isVisible = false
                    binding.emptyStateContainer.isVisible = false
                }
                is UIState.Success -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerViewRestaurantes.isVisible = true
                    binding.emptyStateContainer.isVisible = false
                    establishmentAdapter.submitList(state.data)
                }
                is UIState.Empty -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerViewRestaurantes.isVisible = false
                    binding.emptyStateContainer.isVisible = true
                    binding.textViewError.text = "Nenhum local encontrado."
                }
                is UIState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerViewRestaurantes.isVisible = false
                    binding.emptyStateContainer.isVisible = true
                    binding.textViewError.text = state.message
                }
            }
        }

        // Observers do filtro de cidades (permanecem iguais)
        viewModel.citiesList.observe(viewLifecycleOwner) { cities ->
            if (args.filterType == "category") {
                binding.spinnerCityFilter.isVisible = true
                binding.textViewFilterLabel.isVisible = true
                this.citiesList = cities
                val cityNames = mutableListOf("Todas as Cidades")
                cityNames.addAll(cities.map { it.name })

                val cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cityNames)
                binding.spinnerCityFilter.adapter = cityAdapter
            } else {
                binding.spinnerCityFilter.isVisible = false
                binding.textViewFilterLabel.isVisible = false
            }
        }

        binding.spinnerCityFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            private var isInitialSelection = true
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isInitialSelection) {
                    isInitialSelection = false
                    return
                }
                if (position == 0) {
                    viewModel.onCityFilterChanged(null)
                } else {
                    viewModel.onCityFilterChanged(citiesList[position - 1].id)
                }
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}