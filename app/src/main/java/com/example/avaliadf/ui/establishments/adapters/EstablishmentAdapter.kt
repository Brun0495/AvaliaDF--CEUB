package com.example.avaliadf.ui.establishments.adapters
import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.example.avaliadf.R
import com.example.avaliadf.data.model.Establishment
import com.example.avaliadf.databinding.ItemEstablishmentBinding

class EstablishmentListAdapter(
    private val onItemClick: (establishment: Establishment) -> Unit, // << --- ADICIONE ESTE
    private val onLigarClick: (telefone: String) -> Unit,
    private val onRotaClick: (mapLink: String) -> Unit,
    private val onAvaliarClick: (establishment: Establishment) -> Unit
) : ListAdapter<Establishment, EstablishmentListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onItemClick, onLigarClick, onRotaClick, onAvaliarClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        private val binding: ItemEstablishmentBinding,
        private val onItemClick: (establishment: Establishment) -> Unit, // << --- ADICIONE ESTE
        private val onLigarClick: (telefone: String) -> Unit,
        private val onRotaClick: (mapLink: String) -> Unit,
        private val onAvaliarClick: (establishment: Establishment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Establishment) {
            binding.textViewNomeRestaurante.text = item.name
            binding.textViewHorario.text = "Horário: ${item.hours ?: "Não informado"}"
            binding.textViewTelefone.text = "Telefone: ${item.phone ?: "Não informado"}"
            binding.textViewAvaliacao.text = item.rating?.toString() ?: "N/A"
            val assetPath = "file:///android_asset/TrabProjetoIntegrador/img/${item.imageUrl}"
            Glide.with(binding.root.context).load(assetPath).placeholder(R.drawable.ic_city_placeholder).error(R.drawable.ic_city_placeholder).centerCrop().into(binding.imageViewRestaurante)
            itemView.setOnClickListener {
                onItemClick(item)
            }
            binding.buttonLigar.setOnClickListener { item.phone?.let { onLigarClick(it) } }
            binding.buttonVerRota.setOnClickListener { item.mapLink?.let { onRotaClick(it) } }
            binding.buttonAvaliar.setOnClickListener { onAvaliarClick(item) }

        }
        companion object {
            fun from(parent: ViewGroup,
                     onItemClick: (establishment: Establishment) -> Unit, // << --- ADICIONE ESTE
                     onLigarClick: (telefone: String) -> Unit,
                     onRotaClick: (mapLink: String) -> Unit,
                     onAvaliarClick: (establishment: Establishment) -> Unit): ViewHolder {
                val binding = ItemEstablishmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding, onItemClick ,onLigarClick, onRotaClick, onAvaliarClick)
            }
        }
    }
}
class DiffCallback : DiffUtil.ItemCallback<Establishment>() {
    override fun areItemsTheSame(oldItem: Establishment, newItem: Establishment) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Establishment, newItem: Establishment) = oldItem == newItem
}