package com.example.petcare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.model.Gato
import com.example.petcare.repository.FirebaseRepository
import kotlinx.coroutines.launch

class GatoViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    
    private val _gatos = MutableLiveData<List<Gato>>()
    val gatos: LiveData<List<Gato>> = _gatos
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    private val _success = MutableLiveData<String>()
    val success: LiveData<String> = _success
    
    init {
        loadGatos()
    }
    
    fun loadGatos() {
        viewModelScope.launch {
            _loading.value = true
            repository.getGatos().collect { result ->
                _loading.value = false
                result.fold(
                    onSuccess = { gatos ->
                        _gatos.value = gatos
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Erro ao carregar gatos"
                    }
                )
            }
        }
    }
    
    fun addGato(gato: Gato) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.addGato(gato)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Gato adicionado com sucesso!"
                    loadGatos()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao adicionar Gato"
                }
            )
        }
    }
    
    fun updateGato(gato: Gato) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.updateGato(gato)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Gato atualizado com sucesso!"
                    loadGatos()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao atualizar gato"
                }
            )
        }
    }
    
    fun deleteGato(id: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.deleteGato(id)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Gato excluído com sucesso!"
                    loadGatos()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao excluir gato"
                }
            )
        }
    }
    
    fun clearMessages() {
        _error.value = null
        _success.value = null
    }
} 
