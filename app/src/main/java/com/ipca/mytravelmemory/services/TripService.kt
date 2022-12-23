package com.ipca.mytravelmemory.services

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.HashMap

class TripService {
    // acesso à base de dados
    private val db = Firebase.firestore

    suspend fun create(userID: String, trip: HashMap<String, Any?>): Boolean {
        var isCreated = false

        db.collection("users")
            .document(userID)
            .collection("trips")
            .document()
            .set(trip)
            .addOnSuccessListener {
                isCreated = true
            }
            .addOnFailureListener { e ->
                isCreated = false
            }
            .await()

        return isCreated
    }
}