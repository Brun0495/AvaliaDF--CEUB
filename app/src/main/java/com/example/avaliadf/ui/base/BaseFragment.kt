package com.example.avaliadf.ui.base // Ou o seu pacote de preferência, como ui.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

// Uma assinatura para a função que infla o binding, para deixar o código mais limpo
typealias Inflater<T> = (inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) -> T

/**
 * Fragmento de base abstrato para lidar com a lógica comum de ViewBinding e Edge-to-Edge.
 * Todos os outros fragmentos da app devem herdar desta classe.
 * @param VB O tipo do ViewBinding gerado para o layout do fragmento.
 * @param inflater A função que infla o ViewBinding (ex: FragmentHomeBinding::inflate).
 */
abstract class BaseFragment<VB : ViewBinding>(
    private val inflater: Inflater<VB>
) : Fragment() {

    private var _binding: VB? = null
    // Esta propriedade só é válida entre onCreateView e onDestroyView.
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout usando a função de inflater fornecida pela subclasse
        _binding = this.inflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // APLICA O AJUSTE PARA AS BARRAS DE SISTEMA (EDGE-TO-EDGE) UMA ÚNICA VEZ
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpa a referência do binding para evitar fugas de memória
        _binding = null
    }
}