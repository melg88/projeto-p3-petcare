package com.example.petcare.ui.gatos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.databinding.ItemGatoBinding
import com.example.petcare.model.Gato

class GatosAdapter(
    private val onEditClick: (Gato) -> Unit,
    private val onDeleteClick: (Gato) -> Unit
) : ListAdapter<Gato, GatosAdapter.GatoViewHolder>(GatoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GatoViewHolder {
        val binding = ItemGatoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GatoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GatoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GatoViewHolder(
        private val binding: ItemGatoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gato: Gato) {
            binding.apply {
                tvNomeGato.text = gato.nome
                tvRacaGato.text = gato.raca.ifEmpty { "Raça não informada" }
                tvTutorGato.text = gato.tutorNome.ifEmpty { "Tutor não informado" }
                tvIdadePeso.text = "${gato.idade} anos • ${gato.peso} kg"

                btnEditGato.setOnClickListener {
                    onEditClick(gato)
                }

                btnDeleteGato.setOnClickListener {
                    onDeleteClick(gato)
                }
            }
        }
    }

    private class GatoDiffCallback : DiffUtil.ItemCallback<Gato>() {
        override fun areItemsTheSame(oldItem: Gato, newItem: Gato): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Gato, newItem: Gato): Boolean {
            return oldItem == newItem
        }
    }
} 
