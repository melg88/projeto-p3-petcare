package com.example.petcare.ui.gatos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.databinding.FragmentGatosBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.petcare.viewmodel.GatoViewModel
import com.example.petcare.model.Gato


class GatosFragment : Fragment() {
    private var _binding: FragmentGatosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GatoViewModel by activityViewModels()
    private lateinit var adapter: GatosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGatosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        viewModel.loadGatos()
    }

    private fun setupRecyclerView() {
        adapter = GatosAdapter(
            onEditClick = { gato ->
                showGatoDialog(gato)
            },
            onDeleteClick = { gato ->
                showDeleteConfirmation(gato)
            }
        )
        
        binding.recyclerViewGatos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GatosFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.gatos.observe(viewLifecycleOwner) { gatos ->
            adapter.submitList(gatos)
            binding.emptyState.visibility = if (gatos.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearMessages()
            }
        }

        viewModel.success.observe(viewLifecycleOwner) { success ->
            success?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddGato.setOnClickListener {
            showGatoDialog(null)
        }
    }

    private fun showGatoDialog(gato: Gato?) {
        GatoDialogFragment.newInstance(gato).show(
            parentFragmentManager,
            "GatoDialog"
        )
    }

    private fun showDeleteConfirmation(gato: Gato) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir o gato ${gato.nome}?")
            .setPositiveButton("Excluir") { _, _ ->
                viewModel.deleteGato(gato.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
