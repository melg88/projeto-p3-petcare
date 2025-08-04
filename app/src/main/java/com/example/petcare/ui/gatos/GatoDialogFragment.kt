package com.example.petcare.ui.gatos

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.petcare.databinding.DialogGatoBinding
import com.example.petcare.model.Gato
import com.example.petcare.model.Tutor
import com.example.petcare.viewmodel.GatoViewModel
import com.example.petcare.viewmodel.TutorViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class GatoDialogFragment : DialogFragment() {
    private var _binding: DialogGatoBinding? = null
    private val binding get() = _binding!!

    private val gatoViewModel: GatoViewModel by activityViewModels()
    private val tutorViewModel: TutorViewModel by activityViewModels()
    
    private var gato: Gato? = null
    private var tutores: List<Tutor> = emptyList()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    companion object {
        private const val ARG_GATO = "gato"
        
        fun newInstance(gato: Gato?): GatoDialogFragment {
            return GatoDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_GATO, gato)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGatoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        gato = arguments?.getParcelable(ARG_GATO)
        setupViews()
        loadTutores()
        setupObservers()
    }

    private fun setupViews() {
        binding.apply {
            if (gato != null) {
                // Modo edição
                tvTitle.text = "Editar Gato"
                btnSalvar.text = "Atualizar"
                
                // Preencher campos
                etNomeGato.setText(gato!!.nome)
                etIdadeGato.setText(gato!!.idade.toString())
                etRacaGato.setText(gato!!.raca)
                etPesoGato.setText(gato!!.peso.toString())
                etObservacoesGato.setText(gato!!.observacoes)
            } else {
                // Modo adição
                tvTitle.text = "Adicionar Gato"
                btnSalvar.text = "Salvar"
            }

            btnCancelar.setOnClickListener {
                dismiss()
            }

            btnSalvar.setOnClickListener {
                if (validateFields()) {
                    saveGato()
                }
            }
        }
    }

    private fun loadTutores() {
        tutorViewModel.loadTutores()
    }

    private fun setupObservers() {
        tutorViewModel.tutores.observe(viewLifecycleOwner) { tutores ->
            this.tutores = tutores
            setupTutorSpinner()
        }
    }

    private fun setupTutorSpinner() {
        val tutorNames = tutores.map { it.nome }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tutorNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTutor.adapter = adapter

        // Selecionar tutor atual se estiver editando
        gato?.let { gato ->
            val tutorIndex = tutores.indexOfFirst { it.id == gato.tutorId }
            if (tutorIndex >= 0) {
                binding.spinnerTutor.setSelection(tutorIndex)
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        binding.apply {
            if (etNomeGato.text.isNullOrBlank()) {
                tilNomeGato.error = "Nome é obrigatório"
                isValid = false
            } else {
                tilNomeGato.error = null
            }

            if (etIdadeGato.text.isNullOrBlank()) {
                tilIdadeGato.error = "Idade é obrigatória"
                isValid = false
            } else {
                tilIdadeGato.error = null
            }

            if (etPesoGato.text.isNullOrBlank()) {
                tilPesoGato.error = "Peso é obrigatório"
                isValid = false
            } else {
                tilPesoGato.error = null
            }
        }

        return isValid
    }

    private fun saveGato() {
        val selectedTutorIndex = binding.spinnerTutor.selectedItemPosition
        val selectedTutor = if (selectedTutorIndex >= 0) tutores[selectedTutorIndex] else null

        val novoGato = Gato(
            id = gato?.id ?: "",
            nome = binding.etNomeGato.text.toString(),
            idade = binding.etIdadeGato.text.toString().toIntOrNull() ?: 0,
            raca = binding.etRacaGato.text.toString(),
            peso = binding.etPesoGato.text.toString().toDoubleOrNull() ?: 0.0,
            tutorId = selectedTutor?.id ?: "",
            tutorNome = selectedTutor?.nome ?: "",
            observacoes = binding.etObservacoesGato.text.toString()
        )

        if (gato != null) {
            gatoViewModel.updateGato(novoGato)
        } else {
            gatoViewModel.addGato(novoGato)
        }


        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(), // 90% da largura da tela
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
