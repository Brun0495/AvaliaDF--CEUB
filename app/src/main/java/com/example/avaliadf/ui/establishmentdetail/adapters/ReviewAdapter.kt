package com.example.avaliadf.ui.establishmentdetail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.avaliadf.R
import com.example.avaliadf.data.model.Review
import com.example.avaliadf.databinding.ItemReviewBinding
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            // Nova lógica para as estrelas
            setStars(review.rating.toInt())

            binding.textViewComment.text = review.comment ?: "O usuário não deixou um comentário."

            val dateText = review.timestamp?.toDate()?.let { date ->
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                "por Usuário Anônimo em ${format.format(date)}"
            } ?: "por Usuário Anônimo"

            binding.textViewUserAndDate.text = dateText
        }

        private fun setStars(rating: Int) {
            val stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
            for (i in stars.indices) {
                // A nova lógica: ativa ou desativa a estrela.
                // O seletor no XML vai cuidar de trocar a imagem.
                stars[i].isActivated = i < rating
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}