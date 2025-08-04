package com.example.petcare.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tutor(
    @DocumentId
    val id: String = "",
    val nome: String = "",
    val telefone: String = "",
    val email: String = "",
    val endereco: String = "",
    val dataCadastro: Timestamp = Timestamp.now()
) : Parcelable
