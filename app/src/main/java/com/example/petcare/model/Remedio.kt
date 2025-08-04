package com.example.petcare.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Remedio(
    @DocumentId
    val id: String = "",
    val nome: String = "",
    val dosagem: String = "",
    val frequencia: String = "",
    val dataInicio: Timestamp = Timestamp.now(),
    val dataFim: Timestamp? = null,
    val gatoId: String = "",
    val gatoNome: String = "",
    val observacoes: String = "",
    val dataCadastro: Timestamp = Timestamp.now()
) : Parcelable
