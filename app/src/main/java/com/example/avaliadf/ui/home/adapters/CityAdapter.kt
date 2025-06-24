package com.example.avaliadf.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.avaliadf.R
import com.example.avaliadf.data.model.City
import com.example.avaliadf.databinding.ItemCityBinding

class CityAdapter(private val onItemClicked: (City) -> Unit) : ListAdapter<City, CityAdapter.CityViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(city)
        }
        holder.bind(city)
    }

    class CityViewHolder(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.textViewCityName.text = city.name
            val assetPath = "file:///android_asset/TrabProjetoIntegrador/img/${city.image}"
            Glide.with(itemView.context)
                .load(assetPath)
                .placeholder(R.drawable.ic_city_placeholder)
                .error(R.drawable.ic_city_placeholder)
                .centerCrop()
                .into(binding.imageViewCity)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: City, newItem: City) = oldItem == newItem
    }
}