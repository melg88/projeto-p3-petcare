package com.example.petcare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.model.Vacina
import com.example.petcare.repository.FirebaseRepository
import kotlinx.coroutines.launch

class VacinaViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    
    private val _vacinas = MutableLiveData<List<Vacina>>()
    val vacinas: LiveData<List<Vacina>> = _vacinas
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    private val _success = MutableLiveData<String>()
    val success: LiveData<String> = _success
    
    init {
        loadVacinas()
    }
    
    fun loadVacinas() {
        viewModelScope.launch {
            _loading.value = true
            repository.getVacinas().collect { result ->
                _loading.value = false
                result.fold(
                    onSuccess = { vacinas ->
                        _vacinas.value = vacinas
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Erro ao carregar vacinas"
                    }
                )
            }
        }
    }
    
    fun loadVacinasByGato(gatoId: String) {
        viewModelScope.launch {
            _loading.value = true
            repository.getVacinasByGato(gatoId).collect { result ->
                _loading.value = false
                result.fold(
                    onSuccess = { vacinas ->
                        _vacinas.value = vacinas
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Erro ao carregar vacinas"
                    }
                )
            }
        }
    }
    
    fun addVacina(vacina: Vacina) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.addVacina(vacina)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Vacina adicionada com sucesso!"
                    loadVacinas()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao adicionar vacina"
                }
            )
        }
    }
    
    fun updateVacina(vacina: Vacina) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.updateVacina(vacina)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Vacina atualizada com sucesso!"
                    loadVacinas()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao atualizar vacina"
                }
            )
        }
    }
    
    fun deleteVacina(id: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.deleteVacina(id)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Vacina excluída com sucesso!"
                    loadVacinas()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao excluir vacina"
                }
            )
        }
    }
    
    fun clearMessages() {
        _error.value = null
        _success.value = null
    }
} 
