package com.example.petcare.ui.remedios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.R
import com.example.petcare.databinding.FragmentRemediosBinding
import com.example.petcare.model.Remedio
import com.example.petcare.viewmodel.RemedioViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RemediosFragment : Fragment() {

    private var _binding: FragmentRemediosBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: RemedioViewModel by activityViewModels()
    private lateinit var adapter: RemediosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRemediosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        viewModel.loadRemedios()
    }

    private fun setupRecyclerView() {
        adapter = RemediosAdapter(
            onEditClick = { remedio ->
                showRemedioDialog(remedio)
            },
            onDeleteClick = { remedio ->
                showDeleteConfirmationDialog(remedio)
            }
        )
        
        binding.recyclerViewRemedios.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@RemediosFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.remedios.observe(viewLifecycleOwner) { remedios ->
            adapter.submitList(remedios)
            updateEmptyState(remedios.isEmpty())
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearMessages() // ou clearErrorMessage() se tiver
            }
        }

        viewModel.successMessage.observe(viewLifecycleOwner) { success ->
            success?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.clearSuccessMessage()
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddRemedio.setOnClickListener {
            showRemedioDialog()
        }
    }

    private fun showRemedioDialog(remedio: Remedio? = null) {
        RemedioDialogFragment.newInstance(remedio)
            .show(parentFragmentManager, "RemedioDialog")
    }

    private fun showDeleteConfirmationDialog(remedio: Remedio) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_remedio))
            .setMessage(getString(R.string.confirm_delete_remedio, remedio.nome))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteRemedio(remedio.id)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 
