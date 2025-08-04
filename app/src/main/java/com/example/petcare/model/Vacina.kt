package com.example.petcare.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vacina(
    @DocumentId
    val id: String = "",
    val nome: String = "",
    val dataVacina: Timestamp = Timestamp.now(),
    val proximaVacina: Timestamp? = null,
    val gatoId: String = "",
    val gatoNome: String = "",
    val observacoes: String = "",
    val dataCadastro: Timestamp = Timestamp.now()
) : Parcelable
