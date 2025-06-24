// app/src/main/java/com/example/avaliadf/ui/home/adapters/CityAdapter.kt
package com.example.avaliadf.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.avaliadf.R // Importante para R.drawable
import com.example.avaliadf.data.model.City
import com.example.avaliadf.databinding.ItemCityBinding

class CityAdapter(private val onItemClicked: (City) -> Unit) :
    ListAdapter<City, CityAdapter.CityViewHolder>(CityDiffCallback()) {

    // ... (onCreateViewHolder e outras funções como antes) ...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city)
        holder.itemView.setOnClickListener {
            onItemClicked(city)
        }
    }


    class CityViewHolder(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.textViewCityName.text = city.name

            // Assumindo que city.image contém "brasilia.jpg", "guara.jpg", etc.
            // Construímos o caminho completo para o arquivo dentro da pasta assets.
            val assetPath = "file:///android_asset/TrabProjetoIntegrador/img/${city.image}"

            Glide.with(binding.imageViewCity.context)
                .load(assetPath) // << --- MUDANÇA PRINCIPAL AQUI
                .placeholder(R.drawable.ic_city_placeholder) // Estes devem estar em res/drawable
                .error(R.drawable.ic_city_error_placeholder)   // Estes devem estar em res/drawable
                .centerCrop()
                .into(binding.imageViewCity)
        }
    }

    // ... (CityDiffCallback como antes) ...
    class CityDiffCallback : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem == newItem
        }
    }
}