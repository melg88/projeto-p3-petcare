package com.example.petcare.ui.tutores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.databinding.ItemTutorBinding
import com.example.petcare.model.Tutor

class TutoresAdapter(
    private val onEditClick: (Tutor) -> Unit,
    private val onDeleteClick: (Tutor) -> Unit
) : ListAdapter<Tutor, TutoresAdapter.TutorViewHolder>(TutorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
        val binding = ItemTutorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TutorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TutorViewHolder(
        private val binding: ItemTutorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tutor: Tutor) {
            binding.apply {
                tvNome.text = tutor.nome
                tvTelefone.text = tutor.telefone
                tvEmail.text = tutor.email
                tvEndereco.text = tutor.endereco

                btnEdit.setOnClickListener { onEditClick(tutor) }
                btnDelete.setOnClickListener { onDeleteClick(tutor) }
            }
        }
    }

    private class TutorDiffCallback : DiffUtil.ItemCallback<Tutor>() {
        override fun areItemsTheSame(oldItem: Tutor, newItem: Tutor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tutor, newItem: Tutor): Boolean {
            return oldItem == newItem
        }
    }
} 
