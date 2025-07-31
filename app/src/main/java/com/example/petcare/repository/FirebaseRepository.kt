package com.example.petcare.repository

import com.example.petcare.model.Gato
import com.example.petcare.model.Tutor
import com.example.petcare.model.Vacina
import com.example.petcare.model.Remedio
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    
    // Coleções
    private val gatosCollection = db.collection("gatos")
    private val tutoresCollection = db.collection("tutores")
    private val vacinasCollection = db.collection("vacinas")
    private val remediosCollection = db.collection("remedios")
    
    // ========== GATOS ==========
    
    suspend fun getGatos(): Flow<Result<List<Gato>>> = flow {
        try {
            val snapshot = gatosCollection
                .orderBy("nome", Query.Direction.ASCENDING)
                .get()
                .await()
            
            val gatos = snapshot.toObjects(Gato::class.java)
            emit(Result.success(gatos))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getGatoById(id: String): Result<Gato?> {
        return try {
            val document = gatosCollection.document(id).get().await()
            val gato = document.toObject(Gato::class.java)
            Result.success(gato)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addGato(gato: Gato): Result<String> {
        return try {
            val docRef = gatosCollection.add(gato).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateGato(gato: Gato): Result<Unit> {
        return try {
            gatosCollection.document(gato.id).set(gato).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteGato(id: String): Result<Unit> {
        return try {
            gatosCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ========== TUTORES ==========
    
    suspend fun getTutores(): Flow<Result<List<Tutor>>> = flow {
        try {
            val snapshot = tutoresCollection
                .orderBy("nome", Query.Direction.ASCENDING)
                .get()
                .await()
            
            val tutores = snapshot.toObjects(Tutor::class.java)
            emit(Result.success(tutores))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getTutorById(id: String): Result<Tutor?> {
        return try {
            val document = tutoresCollection.document(id).get().await()
            val tutor = document.toObject(Tutor::class.java)
            Result.success(tutor)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addTutor(tutor: Tutor): Result<String> {
        return try {
            val docRef = tutoresCollection.add(tutor).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateTutor(tutor: Tutor): Result<Unit> {
        return try {
            tutoresCollection.document(tutor.id).set(tutor).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteTutor(id: String): Result<Unit> {
        return try {
            tutoresCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ========== VACINAS ==========
    
    suspend fun getVacinas(): Flow<Result<List<Vacina>>> = flow {
        try {
            val snapshot = vacinasCollection
                .orderBy("dataVacina", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val vacinas = snapshot.toObjects(Vacina::class.java)
            emit(Result.success(vacinas))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getVacinasByGato(gatoId: String): Flow<Result<List<Vacina>>> = flow {
        try {
            val snapshot = vacinasCollection
                .whereEqualTo("gatoId", gatoId)
                .orderBy("dataVacina", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val vacinas = snapshot.toObjects(Vacina::class.java)
            emit(Result.success(vacinas))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun addVacina(vacina: Vacina): Result<String> {
        return try {
            val docRef = vacinasCollection.add(vacina).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateVacina(vacina: Vacina): Result<Unit> {
        return try {
            vacinasCollection.document(vacina.id).set(vacina).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteVacina(id: String): Result<Unit> {
        return try {
            vacinasCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ========== REMÉDIOS ==========
    
    suspend fun getRemedios(): Flow<Result<List<Remedio>>> = flow {
        try {
            val snapshot = remediosCollection
                .orderBy("dataInicio", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val remedios = snapshot.toObjects(Remedio::class.java)
            emit(Result.success(remedios))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getRemediosByGato(gatoId: String): Flow<Result<List<Remedio>>> = flow {
        try {
            val snapshot = remediosCollection
                .whereEqualTo("gatoId", gatoId)
                .orderBy("dataInicio", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val remedios = snapshot.toObjects(Remedio::class.java)
            emit(Result.success(remedios))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun addRemedio(remedio: Remedio): Result<String> {
        return try {
            val docRef = remediosCollection.add(remedio).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateRemedio(remedio: Remedio): Result<Unit> {
        return try {
            remediosCollection.document(remedio.id).set(remedio).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteRemedio(id: String): Result<Unit> {
        return try {
            remediosCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 
