package com.example.petcare.ui.tutores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.petcare.R
import com.example.petcare.databinding.DialogTutorBinding
import com.example.petcare.model.Tutor
import com.example.petcare.viewmodel.TutorViewModel

class TutorDialogFragment : DialogFragment() {

    private var _binding: DialogTutorBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: TutorViewModel by viewModels()
    private var tutor: Tutor? = null
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTutorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViews() {
        tutor = arguments?.getParcelable(ARG_TUTOR)
        isEditMode = tutor != null

        binding.tvTitle.text = if (isEditMode) {
            getString(R.string.edit_tutor)
        } else {
            getString(R.string.add_tutor)
        }

        tutor?.let { populateFields(it) }
    }

    private fun setupObservers() {
        viewModel.successMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearSuccessMessage()
                dismiss()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                saveTutor()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun populateFields(tutor: Tutor) {
        binding.apply {
            etNome.setText(tutor.nome)
            etTelefone.setText(tutor.telefone)
            etEmail.setText(tutor.email)
            etEndereco.setText(tutor.endereco)
        }
    }

    private fun validateFields(): Boolean {
        val nome = binding.etNome.text.toString().trim()
        val telefone = binding.etTelefone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val endereco = binding.etEndereco.text.toString().trim()

        if (nome.isEmpty()) {
            binding.etNome.error = getString(R.string.error_nome_required)
            return false
        }

        if (telefone.isEmpty()) {
            binding.etTelefone.error = getString(R.string.error_telefone_required)
            return false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = getString(R.string.error_email_required)
            return false
        }

        if (endereco.isEmpty()) {
            binding.etEndereco.error = getString(R.string.error_endereco_required)
            return false
        }

        return true
    }

    private fun saveTutor() {
        val nome = binding.etNome.text.toString().trim()
        val telefone = binding.etTelefone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val endereco = binding.etEndereco.text.toString().trim()

        val tutorToSave = Tutor(
            id = tutor?.id ?: "",
            nome = nome,
            telefone = telefone,
            email = email,
            endereco = endereco,
            dataCadastro = tutor?.dataCadastro ?: System.currentTimeMillis()
        )

        if (isEditMode) {
            viewModel.updateTutor(tutorToSave)
        } else {
            viewModel.addTutor(tutorToSave)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TUTOR = "tutor"

        fun newInstance(tutor: Tutor? = null): TutorDialogFragment {
            return TutorDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TUTOR, tutor)
                }
            }
        }
    }
} 
