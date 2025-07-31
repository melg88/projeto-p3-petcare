package com.example.petcare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.model.Tutor
import com.example.petcare.repository.FirebaseRepository
import kotlinx.coroutines.launch

class TutorViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    
    private val _tutores = MutableLiveData<List<Tutor>>()
    val tutores: LiveData<List<Tutor>> = _tutores
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    private val _success = MutableLiveData<String>()
    val success: LiveData<String> = _success
    
    init {
        loadTutores()
    }
    
    fun loadTutores() {
        viewModelScope.launch {
            _loading.value = true
            repository.getTutores().collect { result ->
                _loading.value = false
                result.fold(
                    onSuccess = { tutores ->
                        _tutores.value = tutores
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Erro ao carregar tutores"
                    }
                )
            }
        }
    }
    
    fun addTutor(tutor: Tutor) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.addTutor(tutor)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Tutor adicionado com sucesso!"
                    loadTutores()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao adicionar tutor"
                }
            )
        }
    }
    
    fun updateTutor(tutor: Tutor) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.updateTutor(tutor)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Tutor atualizado com sucesso!"
                    loadTutores()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao atualizar tutor"
                }
            )
        }
    }
    
    fun deleteTutor(id: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.deleteTutor(id)
            _loading.value = false
            
            result.fold(
                onSuccess = {
                    _success.value = "Tutor excluído com sucesso!"
                    loadTutores()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erro ao excluir tutor"
                }
            )
        }
    }
    
    fun clearMessages() {
        _error.value = null
        _success.value = null
    }
} 
