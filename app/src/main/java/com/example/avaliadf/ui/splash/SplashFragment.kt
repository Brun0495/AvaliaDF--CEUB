// ui/splash/SplashFragment.kt
package com.example.avaliadf.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.avaliadf.R
import com.example.avaliadf.data.model.AuthStatus
import com.example.avaliadf.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // O botão "Entrar" só é visível/funcional se o usuário não estiver autenticado
        // e o carregamento tiver terminado.
        binding.buttonEnter.setOnClickListener {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }

        splashViewModel.authStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                AuthStatus.Authenticated -> {
                    // Navegar para a tela de categorias (criaremos HomeFragment para isso depois)
                    // Por enquanto, vamos para login para testar o fluxo completo
                    // findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    // Para seguir o fluxo atual do HTML:
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment) // Ou para categorias se já logado
                    // Se for para categorias direto:
                    // findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                }
                AuthStatus.Unauthenticated -> {
                    binding.progressBarSplash.visibility = View.GONE
                    binding.buttonEnter.visibility = View.VISIBLE
                    binding.imageViewLogo.visibility = View.VISIBLE
                }
                AuthStatus.Loading -> {
                    binding.progressBarSplash.visibility = View.VISIBLE
                    binding.buttonEnter.visibility = View.GONE // Esconde o botão enquanto carrega
                    binding.imageViewLogo.visibility = View.VISIBLE // Mantém a logo visível
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}