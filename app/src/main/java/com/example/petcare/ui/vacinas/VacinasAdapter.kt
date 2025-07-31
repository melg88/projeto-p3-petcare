package com.example.petcare.ui.vacinas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.databinding.ItemVacinaBinding
import com.example.petcare.model.Vacina
import java.text.SimpleDateFormat
import java.util.*

class VacinasAdapter(
    private val onEditClick: (Vacina) -> Unit,
    private val onDeleteClick: (Vacina) -> Unit
) : ListAdapter<Vacina, VacinasAdapter.VacinaViewHolder>(VacinaDiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacinaViewHolder {
        val binding = ItemVacinaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VacinaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacinaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VacinaViewHolder(
        private val binding: ItemVacinaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacina: Vacina) {
            binding.apply {
                tvNome.text = vacina.nome
                tvGato.text = "Gato: ${vacina.gatoNome}"
                tvDataVacina.text = "Data: ${formatDate(vacina.dataVacina)}"
                tvProximaVacina.text = "Próxima: ${formatDate(vacina.proximaVacina)}"
                
                if (vacina.observacoes.isNotEmpty()) {
                    tvObservacoes.text = "Observações: ${vacina.observacoes}"
                    tvObservacoes.visibility = android.view.View.VISIBLE
                } else {
                    tvObservacoes.visibility = android.view.View.GONE
                }

                btnEdit.setOnClickListener { onEditClick(vacina) }
                btnDelete.setOnClickListener { onDeleteClick(vacina) }
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

    private class VacinaDiffCallback : DiffUtil.ItemCallback<Vacina>() {
        override fun areItemsTheSame(oldItem: Vacina, newItem: Vacina): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vacina, newItem: Vacina): Boolean {
            return oldItem == newItem
        }
    }
} 
