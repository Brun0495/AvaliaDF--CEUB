// Dentro de ui/main/MainActivity.kt
package com.example.avaliadf.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController // Se você for usar ActionBar
import com.example.avaliadf.R
import com.example.avaliadf.databinding.ActivityMainBinding // Importe o ViewBinding gerado

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding // Variável para o ViewBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar o layout usando ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use binding.root

        // Configurar o NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Exemplo: Se você tiver uma ActionBar e quiser que o título mude com a navegação
        // setupActionBarWithNavController(navController)

        // Ajustar o padding para o conteúdo dentro do FragmentContainerView (agora no root do binding)
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainContainer) { v, insets -> // Use o ID do root layout
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Ajuste o padding do container principal, os fragments internos cuidarão de seus próprios paddings se necessário
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Se você usar setupActionBarWithNavController, precisa dar override neste método
    // override fun onSupportNavigateUp(): Boolean {
    //     return navController.navigateUp() || super.onSupportNavigateUp()
    // }
}