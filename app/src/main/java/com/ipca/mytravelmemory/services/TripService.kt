package com.ipca.mytravelmemory.services

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.HashMap

class TripService {
    // acesso à base de dados
    private val db = Firebase.firestore

    fun create(
        userID: String,
        trip: HashMap<String, Any?>,
        scope: CoroutineScope,
        callback: (Boolean) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            db.collection("users")
                .document(userID)
                .collection("trips")
                .document()
                .set(trip)
                .addOnSuccessListener {
                    scope.launch(Dispatchers.Main) {
                        // retornar resultado da operação
                        callback(true)
                    }
                }
                .addOnFailureListener { error ->
                    scope.launch(Dispatchers.Main) {
                        callback(false)
                    }
                }
        }
    }
}