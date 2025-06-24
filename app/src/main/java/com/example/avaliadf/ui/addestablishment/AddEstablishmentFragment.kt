package com.example.avaliadf.ui.addestablishment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.avaliadf.data.model.City
import com.example.avaliadf.data.repository.*
import com.example.avaliadf.databinding.FragmentAddEstablishmentBinding
import com.example.avaliadf.ui.base.BaseFragment // IMPORTAR

// 1. MUDAR A HERANÇA DA CLASSE
class AddEstablishmentFragment : BaseFragment<FragmentAddEstablishmentBinding>(FragmentAddEstablishmentBinding::inflate) {

    // 2. REMOVER _binding e binding
    // private var _binding: FragmentAddEstablishmentBinding? = null
    // private val binding get() = _binding!!

    private lateinit var viewModel: AddEstablishmentViewModel
    private var citiesList: List<City> = emptyList()

    // 3. REMOVER onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 4. CHAMAR super.onViewCreated() PRIMEIRO
        super.onViewCreated(view, savedInstanceState)

        // O resto da sua lógica continua intacta
        val establishmentRepository: EstablishmentRepository = FirestoreEstablishmentRepositoryImpl()
        val cityRepository: CityRepository = FirestoreCityRepositoryImpl()
        val factory = AddEstablishmentViewModelFactory(establishmentRepository, cityRepository)
        viewModel = ViewModelProvider(this, factory)[AddEstablishmentViewModel::class.java]

        setupToolbar()
        setupSpinners()
        setupClickListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbarAddEstablishment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSpinners() {
        val categories = listOf("restaurante", "hotel", "lazer")
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategory.adapter = categoryAdapter
    }

    private fun setupClickListeners() {
        binding.buttonAddEstablishment.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val selectedCityPosition = binding.spinnerCity.selectedItemPosition
            val cityId = if (selectedCityPosition >= 0 && selectedCityPosition < citiesList.size) {
                citiesList[selectedCityPosition].id
            } else ""

            val address = binding.editTextAddress.text.toString()
            val phone = binding.editTextPhone.text.toString()
            val hours = binding.editTextHours.text.toString()
            val description = binding.editTextDescription.text.toString()

            viewModel.addEstablishment(name, category, cityId, address, phone, hours, null, description)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { binding.progressBarAdd.isVisible = it }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, "Erro: $error", Toast.LENGTH_LONG).show()
            }
        }
        viewModel.addSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Estabelecimento cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        viewModel.cities.observe(viewLifecycleOwner) { cities ->
            this.citiesList = cities
            val cityNames = cities.map { it.name }
            val cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cityNames)
            binding.spinnerCity.adapter = cityAdapter
        }
    }

    // 5. REMOVER onDestroyView
}