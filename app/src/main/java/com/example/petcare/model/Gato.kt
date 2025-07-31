package com.example.petcare.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Gato(
    @DocumentId
    val id: String = "",
    val nome: String = "",
    val idade: Int = 0,
    val raca: String = "",
    val peso: Double = 0.0,
    val tutorId: String = "",
    val tutorNome: String = "",
    val observacoes: String = "",
    val dataCadastro: Timestamp = Timestamp.now(),
    val fotoUrl: String = ""
) 
