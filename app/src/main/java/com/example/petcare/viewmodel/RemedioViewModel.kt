package com.example.petcare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.model.Remedio
import com.example.petcare.repository.FirebaseRepository
import kotlinx.coroutines.launch

class RemedioViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _remedios = MutableLiveData<List<Remedio>>()
    val remedios: LiveData<List<Remedio>> = _remedios

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _success = MutableLiveData<String>()
    val success: LiveData<String> = _success

    init {
        loadRemedios()
    }

    fun loadRemedios() {
        viewModelScope.launch {
            _loading.value = true
            repository.getRemedios().collect { result ->
                _loading.value = false
                result.fold(
                    onSuccess = { remedios ->
                        _remedios.value = remedios
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Erro ao carregar remédios"
                    }
                )
            }
        }
    }

    fun loadRemediosByGato(gatoId: String) {
        viewModelScope.launch {
            _loading.value = true
            repository.getRemediosByGato(gatoId).collect { result ->
                _loading.value = false
                result.fold(
                    onSuccess = { remedios ->
                        _remedios.value = remedios
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Erro ao carregar remédios"
                    }
                )
            }
        }
    }

    fun addRemedio(remedio: Remedio) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.addRemedio(remedio)
            _loading.value = false

            result.fold(
                onSuccess = {
                    _success.value = "Remédio adicionado com sucesso!"
                    loadRemedios()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao adicionar remédio"
                }
            )
        }
    }

    fun updateRemedio(remedio: Remedio) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.updateRemedio(remedio)
            _loading.value = false

            result.fold(
                onSuccess = {
                    _success.value = "Remédio atualizado com sucesso!"
                    loadRemedios()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao atualizar remédio"
                }
            )
        }
    }

    fun deleteRemedio(id: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.deleteRemedio(id)
            _loading.value = false

            result.fold(
                onSuccess = {
                    _success.value = "Remédio excluído com sucesso!"
                    loadRemedios()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao excluir remédio"
                }
            )
        }
    }
    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> get() = _successMessage
    fun clearMessages() {
        _error.value = null
        _success.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }
    private fun notifySuccess(message: String) {
        _successMessage.value = message
    }
}
