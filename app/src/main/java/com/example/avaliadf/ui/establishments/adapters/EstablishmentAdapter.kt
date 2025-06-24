package com.example.avaliadf.ui.establishments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.avaliadf.R
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.databinding.ItemEstablishmentBinding

class EstablishmentListAdapter(
    private val onItemClick: (establishment: Establishment) -> Unit,
    private val onLigarClick: (telefone: String) -> Unit,
    private val onRotaClick: (mapLink: String) -> Unit
) : ListAdapter<Establishment, EstablishmentListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEstablishmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick, onLigarClick, onRotaClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemEstablishmentBinding,
        private val onItemClick: (establishment: Establishment) -> Unit,
        private val onLigarClick: (telefone: String) -> Unit,
        private val onRotaClick: (mapLink: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Establishment) {
            binding.textViewNomeRestaurante.text = item.name
            binding.textViewAddress.text = item.address
            binding.textViewHorario.text = "Horário: ${item.hours ?: "Não informado"}"
            binding.textViewTelefone.text = "Telefone: ${item.phone ?: "Não informado"}"
            binding.textViewAvaliacao.text = String.format("%.1f", item.rating)

            val assetPath = "file:///android_asset/TrabProjetoIntegrador/img/${item.imageUrl}"
            Glide.with(itemView.context).load(assetPath)
                .placeholder(R.drawable.ic_city_placeholder)
                .error(R.drawable.ic_city_placeholder)
                .centerCrop().into(binding.imageViewRestaurante)

            // O botão de avaliar na lista principal não é mais necessário
            binding.buttonAvaliar.isVisible = false

            // Configuração dos cliques
            itemView.setOnClickListener { onItemClick(item) }
            binding.buttonLigar.setOnClickListener { item.phone?.let { onLigarClick(it) } }
            binding.buttonVerRota.setOnClickListener { item.mapLink?.let { onRotaClick(it) } }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Establishment>() {
        override fun areItemsTheSame(oldItem: Establishment, newItem: Establishment) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Establishment, newItem: Establishment) = oldItem == newItem
    }
}