package com.example.petcare.ui.vacinas

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
import com.example.petcare.databinding.DialogVacinaBinding
import com.example.petcare.model.Gato
import com.example.petcare.model.Vacina
import com.example.petcare.viewmodel.GatoViewModel
import com.example.petcare.viewmodel.VacinaViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class VacinaDialogFragment : DialogFragment() {

    private var _binding: DialogVacinaBinding? = null
    private val binding get() = _binding!!
    
    private val vacinaViewModel: VacinaViewModel by activityViewModels()
    private val gatoViewModel: GatoViewModel by activityViewModels()
    
    private var vacina: Vacina? = null
    private var isEditMode = false
    private var selectedGato: Gato? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogVacinaBinding.inflate(inflater, container, false)
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
        vacina = arguments?.getParcelable(ARG_VACINA)
        isEditMode = vacina != null

        binding.tvTitle.text = if (isEditMode) {
            getString(R.string.edit_vacina)
        } else {
            getString(R.string.add_vacina)
        }

        vacina?.let { populateFields(it) }
    }

    private fun setupObservers() {
        vacinaViewModel.success.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                vacinaViewModel.clearMessages()
                dismiss()
            }
        }

        vacinaViewModel.error.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                vacinaViewModel.clearMessages()
            }
        }

        gatoViewModel.gatos.observe(viewLifecycleOwner) { gatos ->
            setupGatoSpinner(gatos)
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                saveVacina()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.etDataVacina.setOnClickListener {
            showDatePicker { timestamp ->
                binding.etDataVacina.setText(dateFormat.format(Date(timestamp)))
            }
        }

        binding.etProximaVacina.setOnClickListener {
            showDatePicker { timestamp ->
                binding.etProximaVacina.setText(dateFormat.format(Date(timestamp)))
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
        vacina?.let { vacina ->
            val gatoIndex = gatos.indexOfFirst { it.id == vacina.gatoId }
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

    private fun populateFields(vacina: Vacina) {
        binding.apply {
            etNome.setText(vacina.nome)
            etDataVacina.setText(formatDate(vacina.dataVacina))
            etProximaVacina.setText(formatDate(vacina.proximaVacina))
            etObservacoes.setText(vacina.observacoes)
        }
    }

    private fun validateFields(): Boolean {
        val nome = binding.etNome.text.toString().trim()
        val dataVacina = binding.etDataVacina.text.toString().trim()

        if (nome.isEmpty()) {
            binding.etNome.error = getString(R.string.error_nome_required)
            return false
        }

        if (dataVacina.isEmpty()) {
            binding.etDataVacina.error = getString(R.string.error_data_required)
            return false
        }

        if (selectedGato == null) {
            Toast.makeText(requireContext(), getString(R.string.error_gato_required), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveVacina() {
        val nome = binding.etNome.text.toString().trim()
        val dataVacina = parseDate(binding.etDataVacina.text.toString().trim())
        val proximaVacina = parseDate(binding.etProximaVacina.text.toString().trim())
        val observacoes = binding.etObservacoes.text.toString().trim()

        val vacinaToSave = Vacina(
            id = vacina?.id ?: "",
            nome = nome,
            dataVacina = Timestamp(dataVacina / 1000, 0),
            proximaVacina = Timestamp(proximaVacina / 1000, 0),
            gatoId = selectedGato!!.id,
            gatoNome = selectedGato!!.nome,
            observacoes = observacoes,
            dataCadastro = vacina?.dataCadastro ?: Timestamp.now()
        )

        if (isEditMode) {
            vacinaViewModel.updateVacina(vacinaToSave)
        } else {
            vacinaViewModel.addVacina(vacinaToSave)
        }
        dismiss()
    }

    private fun formatDate(timestamp: com.google.firebase.Timestamp?): String {
        return if (timestamp != null) {
            dateFormat.format(timestamp.toDate()) // converte para Date e formata
        } else {
            "Não definida"
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
        private const val ARG_VACINA = "vacina"

        fun newInstance(vacina: Vacina? = null): VacinaDialogFragment {
            return VacinaDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_VACINA, vacina)
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
