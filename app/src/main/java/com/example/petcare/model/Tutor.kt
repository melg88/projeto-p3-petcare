package com.example.petcare.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Tutor(
    @DocumentId
    val id: String = "",
    val nome: String = "",
    val telefone: String = "",
    val email: String = "",
    val endereco: String = "",
    val dataCadastro: Timestamp = Timestamp.now()
) 
