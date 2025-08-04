package com.example.petcare.ui.remedios

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.petcare.R
import com.example.petcare.databinding.DialogRemedioBinding
import com.example.petcare.model.Gato
import com.example.petcare.model.Remedio
import com.example.petcare.viewmodel.GatoViewModel
import com.example.petcare.viewmodel.RemedioViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.Timestamp

class RemedioDialogFragment : DialogFragment() {

    private var _binding: DialogRemedioBinding? = null
    private val binding get() = _binding!!
    
    private val remedioViewModel: RemedioViewModel by activityViewModels()
    private val gatoViewModel: GatoViewModel by activityViewModels()
    
    private var remedio: Remedio? = null
    private var isEditMode = false
    private var selectedGato: Gato? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRemedioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupObservers()
        setupClickListeners()
        loadGatos()
    }

    private fun setupViews() {
        remedio = arguments?.getParcelable(ARG_REMEDIO)
        isEditMode = remedio != null

        binding.tvTitle.text = if (isEditMode) {
            getString(R.string.edit_remedio)
        } else {
            getString(R.string.add_remedio)
        }

        remedio?.let { populateFields(it) }
    }

    private fun setupObservers() {
        remedioViewModel.successMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                remedioViewModel.clearSuccessMessage()
                dismiss()
            }
        }

        remedioViewModel.error.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                remedioViewModel.clearMessages()
            }
        }

        gatoViewModel.gatos.observe(viewLifecycleOwner) { gatos ->
            setupGatoSpinner(gatos)
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                saveRemedio()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.etDataInicio.setOnClickListener {
            showDatePicker { timestamp ->
                binding.etDataInicio.setText(dateFormat.format(Date(timestamp)))
            }
        }

        binding.etDataFim.setOnClickListener {
            showDatePicker { timestamp ->
                binding.etDataFim.setText(dateFormat.format(Date(timestamp)))
            }
        }
    }

    private fun loadGatos() {
        gatoViewModel.loadGatos()
    }

    private fun setupGatoSpinner(gatos: List<Gato>) {
        val gatoNames = gatos.map { it.nome }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gatoNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGato.adapter = adapter

        binding.spinnerGato.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGato = gatos[position]
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                selectedGato = null
            }
        })

        // Se estiver editando, selecionar o gato correto
        remedio?.let { remedio ->
            val gatoIndex = gatos.indexOfFirst { it.id == remedio.gatoId }
            if (gatoIndex >= 0) {
                binding.spinnerGato.setSelection(gatoIndex)
            }
        }
    }

    private fun showDatePicker(onDateSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun populateFields(remedio: Remedio) {
        binding.apply {
            etNome.setText(remedio.nome)
            etDosagem.setText(remedio.dosagem)
            etFrequencia.setText(remedio.frequencia)
            etDataInicio.setText(formatDate(remedio.dataInicio.seconds * 1000))
            etDataFim.setText(
                remedio.dataFim?.seconds?.let { formatDate(it * 1000) } ?: ""
            )
            etObservacoes.setText(remedio.observacoes)
        }
    }

    private fun validateFields(): Boolean {
        val nome = binding.etNome.text.toString().trim()
        val dosagem = binding.etDosagem.text.toString().trim()
        val frequencia = binding.etFrequencia.text.toString().trim()
        val dataInicio = binding.etDataInicio.text.toString().trim()

        if (nome.isEmpty()) {
            binding.etNome.error = getString(R.string.error_nome_required)
            return false
        }

        if (dosagem.isEmpty()) {
            binding.etDosagem.error = getString(R.string.error_dosagem_required)
            return false
        }

        if (frequencia.isEmpty()) {
            binding.etFrequencia.error = getString(R.string.error_frequencia_required)
            return false
        }

        if (dataInicio.isEmpty()) {
            binding.etDataInicio.error = getString(R.string.error_data_required)
            return false
        }

        if (selectedGato == null) {
            Toast.makeText(requireContext(), getString(R.string.error_gato_required), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveRemedio() {
        val nome = binding.etNome.text.toString().trim()
        val dosagem = binding.etDosagem.text.toString().trim()
        val frequencia = binding.etFrequencia.text.toString().trim()
        val dataInicio = parseDate(binding.etDataInicio.text.toString().trim())
        val dataFim = parseDate(binding.etDataFim.text.toString().trim())
        val observacoes = binding.etObservacoes.text.toString().trim()

        val remedioToSave = Remedio(
            id = remedio?.id ?: "",
            nome = nome,
            dosagem = dosagem,
            frequencia = frequencia,
            dataInicio = Timestamp(dataInicio / 1000, 0),
            dataFim = if (dataFim > 0) Timestamp(dataFim / 1000, 0) else null,
            gatoId = selectedGato!!.id,
            gatoNome = selectedGato!!.nome,
            observacoes = observacoes,
            dataCadastro = remedio?.dataCadastro ?: Timestamp.now()
        )

        if (isEditMode) {
            remedioViewModel.updateRemedio(remedioToSave)
        } else {
            remedioViewModel.addRemedio(remedioToSave)
        }

        dismiss()

    }

    private fun formatDate(timestamp: Long): String {
        return if (timestamp > 0) {
            dateFormat.format(Date(timestamp))
        } else {
            ""
        }
    }

    private fun parseDate(dateString: String): Long {
        return try {
            if (dateString.isNotEmpty()) {
                dateFormat.parse(dateString)?.time ?: 0
            } else {
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_REMEDIO = "remedio"

        fun newInstance(remedio: Remedio? = null): RemedioDialogFragment {
            return RemedioDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_REMEDIO, remedio)
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(), // 90% da largura da tela
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
} 
