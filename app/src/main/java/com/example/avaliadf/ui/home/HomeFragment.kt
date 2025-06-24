// app/src/main/java/com/example/avaliadf/ui/home/HomeFragment.kt
package com.example.avaliadf.ui.home

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.avaliadf.R
import com.example.avaliadf.data.repository.AuthRepository
import com.example.avaliadf.data.repository.CityRepository
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl
import com.example.avaliadf.data.repository.LocalCityRepositoryImpl // Importa o repositório local de cidades
import com.example.avaliadf.databinding.FragmentHomeBinding
import com.example.avaliadf.ui.home.adapters.CityAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var cityAdapter: CityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o ViewModel usando a factory correta e os repositórios locais para o protótipo
        val authRepository: AuthRepository = FirebaseAuthRepositoryImpl()
        val cityRepository: CityRepository = LocalCityRepositoryImpl() // Usando o repositório local
        val factory = HomeViewModelFactory(authRepository, cityRepository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupToolbar()
        setupCategoryButtons()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        cityAdapter = CityAdapter { city ->
            // Ação de clique para cada item da cidade
            // Usa o Safe Args (HomeFragmentDirections) para criar a ação de navegação com argumentos
            val action = HomeFragmentDirections.actionHomeFragmentToEstablishmentListFragment(
                filterType = "city",
                // Usamos o ID da cidade (ex: "aguasclaras") como o valor do filtro
                filterValue = city.id
            )
            findNavController().navigate(action)
        }

        binding.recyclerViewCities.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cityAdapter
        }
    }

    private fun setupCategoryButtons() {
        // Para cada botão, usamos o Safe Args para navegar para a mesma tela,
        // mas passando argumentos de filtro diferentes.
        binding.buttonCategoryRestaurante.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEstablishmentListFragment(
                filterType = "category",
                filterValue = "restaurante"
            )
            findNavController().navigate(action)
        }
        binding.buttonCategoryLazer.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEstablishmentListFragment(
                filterType = "category",
                filterValue = "lazer"
            )
            findNavController().navigate(action)
        }
        binding.buttonCategoryHotel.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEstablishmentListFragment(
                filterType = "category",
                filterValue = "hotel"
            )
            findNavController().navigate(action)
        }
    }

    private fun setupObservers() {
        // Observador para o logout
        // ---- REMOVEMOS O OBSERVER DO "logoutComplete" ----

        // Este agora é o ÚNICO observer que trata de logout
        homeViewModel.authStatus.observe(viewLifecycleOwner) { authResult ->
            // Se o resultado não for de sucesso E o código for de logout
            if (!authResult.success && authResult.errorCode == "SIGNED_OUT") {
                // E se já não estivermos na tela de login (para evitar navegação dupla)
                if (findNavController().currentDestination?.id == R.id.homeFragment) {
                    Toast.makeText(context, "Você foi desconectado.", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment_on_logout)
                }
            }
        }

        // Observador para a lista de cidades vinda do ViewModel
        homeViewModel.citiesList.observe(viewLifecycleOwner) { cities ->
            binding.recyclerViewCities.isVisible = cities.isNotEmpty()
            binding.textViewCitiesMessage.isVisible = cities.isEmpty()
            if (cities.isEmpty()){
                binding.textViewCitiesMessage.text = "Nenhuma cidade encontrada."
            }
            cityAdapter.submitList(cities)
        }
    }

    private fun setupToolbar() {
        binding.homeToolbar.inflateMenu(R.menu.home_toolbar_menu)
        binding.homeToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_profile_menu -> {
                    showProfilePopupMenu(binding.homeToolbar.findViewById(R.id.action_profile_menu))
                    true
                }
                else -> false
            }
        }
    }

    private fun showProfilePopupMenu(anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.profile_options_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.option_my_profile -> {
                    // ANTES: Toast.makeText(context, "Navegar para Meu Perfil", Toast.LENGTH_SHORT).show()
                    // DEPOIS:
                    findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
                    true
                }
                R.id.option_settings -> {
                    Toast.makeText(context, "Navegar para Configurações", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.option_my_avaliacoins -> {
                    Toast.makeText(context, "Navegar para Meus AvaliaCoins", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.option_logout -> {
                    homeViewModel.logoutUser()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}