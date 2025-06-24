// ui/auth/LoginFragment.kt
package com.example.avaliadf.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible // << ADICIONE ESTE IMPORT
import androidx.fragment.app.Fragment
// Remova viewModels se não for usar a forma padrão sem factory
import androidx.lifecycle.ViewModelProvider // << ADICIONE ESTE IMPORT
import androidx.navigation.fragment.findNavController
import com.example.avaliadf.R
import com.example.avaliadf.data.repository.FirebaseAuthRepositoryImpl // << ADICIONE ESTE IMPORT
import com.example.avaliadf.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel // << MUDANÇA AQUI (não usar by viewModels() diretamente sem factory configurada)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- INICIALIZAÇÃO DO VIEWMODEL COM FACTORY ---
        val repository = FirebaseAuthRepositoryImpl()
        val factory = AuthViewModelFactory(repository) // << AGORA SÓ PRECISA DO AuthRepository
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        // --- FIM DA INICIALIZAÇÃO ---

        binding.buttonAccess.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Preencha e-mail e senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // A UI de loading será controlada pelo observer de loginViewModel.isLoading
            loginViewModel.signIn(email, password) // << MUDANÇA AQUI: loginUser para signIn
        }

        binding.textViewCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment) // << DESCOMENTE E VERIFIQUE ID DA ACTION
        }

        binding.textViewForgotPassword.setOnClickListener {
            // findNavController().navigate(R.id.action_loginFragment_to_passwordRecoveryFragment) // Criar esta ação/fragmento depois
            Toast.makeText(context, "Navegar para Recuperar Senha (a implementar)", Toast.LENGTH_SHORT).show()
        }

        binding.buttonLoginGoogle.setOnClickListener {
            Toast.makeText(context, "Login com Google desativado no momento (MVP)", Toast.LENGTH_LONG).show()
        }

        // --- OBSERVER PARA O ESTADO DE CARREGAMENTO ---
        loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarLogin.isVisible = isLoading // Assumindo que você tem um progressBarLogin no seu XML
            binding.buttonAccess.isEnabled = !isLoading
        }
        // --- FIM DO OBSERVER DE CARREGAMENTO ---

        // --- OBSERVER PARA O RESULTADO DO LOGIN ---
        loginViewModel.authResultLiveData.observe(viewLifecycleOwner) { result -> // << MUDANÇA AQUI: authResult para authResultLiveData
            // O ViewModel deve setar isLoading = false quando o resultado é recebido
            // ou podemos fazer isso aqui explicitamente se o ViewModel não o fizer.
            // Se o ViewModel não definir _isLoading.value = false após a chamada do repositório,
            // podemos chamar loginViewModel.setLoading(false) aqui ou diretamente:
            // (mas o ideal é o ViewModel controlar seu estado)
            // Exemplo: (Se o ViewModel não mudar isLoading para false)
            // if (binding.progressBarLogin.isVisible) {
            //     loginViewModel.notifyLoadingComplete() // Uma função hipotética no ViewModel
            // }


            if (result.success) { // Agora 'success' deve ser resolvido
                Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                // result.errorCode e result.errorMessage agora devem ser resolvidos
                val errorMessage = when (result.errorCode) {
                    "auth/user-not-found" -> "Nenhum usuário encontrado com este e-mail."
                    "auth/wrong-password" -> "Senha incorreta."
                    "auth/invalid-email" -> "E-mail inválido."
                    "auth/too-many-requests" -> "Muitas tentativas de login. Tente novamente mais tarde."
                    else -> result.errorMessage ?: "Erro ao fazer login."
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}