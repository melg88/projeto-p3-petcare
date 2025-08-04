package com.example.petcare.ui.tutores

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
import com.example.petcare.databinding.FragmentTutoresBinding
import com.example.petcare.model.Tutor
import com.example.petcare.viewmodel.TutorViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TutoresFragment : Fragment() {
    private var _binding: FragmentTutoresBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TutorViewModel by activityViewModels()
    private lateinit var adapter: TutoresAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTutoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        viewModel.loadTutores()
    }

    private fun setupRecyclerView() {
        adapter = TutoresAdapter(
            onEditClick = { tutor ->
                showTutorDialog(tutor)
            },
            onDeleteClick = { tutor ->
                showDeleteConfirmation(tutor)
            }
        )
        
        binding.recyclerViewTutores.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TutoresFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.tutores.observe(viewLifecycleOwner) { tutores ->
            adapter.submitList(tutores)
            updateEmptyState(tutores.isEmpty())
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
        binding.fabAddTutor.setOnClickListener {
            showTutorDialog()
        }
    }

    private fun showTutorDialog(tutor: Tutor? = null) {
        TutorDialogFragment.newInstance(tutor)
            .show(parentFragmentManager, "TutorDialog")
    }

    private fun showDeleteConfirmation(tutor: Tutor) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_tutor))
            .setMessage(getString(R.string.confirm_delete_tutor, tutor.nome))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteTutor(tutor.id)
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
