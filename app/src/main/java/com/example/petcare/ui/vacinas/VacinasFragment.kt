package com.example.petcare.ui.vacinas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.R
import com.example.petcare.databinding.FragmentVacinasBinding
import com.example.petcare.model.Vacina
import com.example.petcare.viewmodel.VacinaViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VacinasFragment : Fragment() {

    private var _binding: FragmentVacinasBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: VacinaViewModel by viewModels()
    private lateinit var adapter: VacinasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacinasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        viewModel.loadVacinas()
    }

    private fun setupRecyclerView() {
        adapter = VacinasAdapter(
            onEditClick = { vacina ->
                showVacinaDialog(vacina)
            },
            onDeleteClick = { vacina ->
                showDeleteConfirmationDialog(vacina)
            }
        )
        
        binding.recyclerViewVacinas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@VacinasFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.vacinas.observe(viewLifecycleOwner) { vacinas ->
            adapter.submitList(vacinas)
            updateEmptyState(vacinas.isEmpty())
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
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
        binding.fabAddVacina.setOnClickListener {
            showVacinaDialog()
        }
    }

    private fun showVacinaDialog(vacina: Vacina? = null) {
        VacinaDialogFragment.newInstance(vacina)
            .show(childFragmentManager, "VacinaDialog")
    }

    private fun showDeleteConfirmationDialog(vacina: Vacina) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_vacina))
            .setMessage(getString(R.string.confirm_delete_vacina, vacina.nome))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteVacina(vacina)
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
