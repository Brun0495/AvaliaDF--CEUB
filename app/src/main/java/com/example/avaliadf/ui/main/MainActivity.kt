package com.example.avaliadf.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.avaliadf.R
import com.example.avaliadf.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // A splash screen deve ser a PRIMEIRA coisa a ser chamada.
        installSplashScreen()

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Vamos remover o enableEdgeToEdge por enquanto,
        // pois ele pode interferir na transição da splash screen.


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Chamamos a nova função que configura a navegação e o destino inicial.
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Pega o grafo de navegação
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        // --- LÓGICA DE DIRECIONAMENTO ---
        // Verifica se há um usuário logado no Firebase
        if (FirebaseAuth.getInstance().currentUser != null) {
            // Se SIM, a tela inicial do grafo será a Home
            navGraph.setStartDestination(R.id.homeFragment)
        } else {
            // Se NÃO, a tela inicial do grafo será o Login
            navGraph.setStartDestination(R.id.loginFragment)
        }

        // Define o grafo modificado no NavController
        navController.graph = navGraph
    }
}