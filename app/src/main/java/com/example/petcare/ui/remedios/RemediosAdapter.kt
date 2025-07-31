package com.example.petcare.ui.remedios

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.databinding.ItemRemedioBinding
import com.example.petcare.model.Remedio
import java.text.SimpleDateFormat
import java.util.*

class RemediosAdapter(
    private val onEditClick: (Remedio) -> Unit,
    private val onDeleteClick: (Remedio) -> Unit
) : ListAdapter<Remedio, RemediosAdapter.RemedioViewHolder>(RemedioDiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemedioViewHolder {
        val binding = ItemRemedioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RemedioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RemedioViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RemedioViewHolder(
        private val binding: ItemRemedioBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(remedio: Remedio) {
            binding.apply {
                tvNome.text = remedio.nome
                tvGato.text = "Gato: ${remedio.gatoNome}"
                tvDosagem.text = "Dosagem: ${remedio.dosagem}"
                tvFrequencia.text = "Frequência: ${remedio.frequencia}"
                tvPeriodo.text = "Período: ${formatDate(remedio.dataInicio)} - ${formatDate(remedio.dataFim)}"
                
                if (remedio.observacoes.isNotEmpty()) {
                    tvObservacoes.text = "Observações: ${remedio.observacoes}"
                    tvObservacoes.visibility = android.view.View.VISIBLE
                } else {
                    tvObservacoes.visibility = android.view.View.GONE
                }

                btnEdit.setOnClickListener { onEditClick(remedio) }
                btnDelete.setOnClickListener { onDeleteClick(remedio) }
            }
        }

        private fun formatDate(timestamp: Long): String {
            return if (timestamp > 0) {
                dateFormat.format(Date(timestamp))
            } else {
                "Não definida"
            }
        }
    }

    private class RemedioDiffCallback : DiffUtil.ItemCallback<Remedio>() {
        override fun areItemsTheSame(oldItem: Remedio, newItem: Remedio): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Remedio, newItem: Remedio): Boolean {
            return oldItem == newItem
        }
    }
} 
