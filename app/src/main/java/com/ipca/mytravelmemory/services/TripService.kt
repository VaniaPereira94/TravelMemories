package com.ipca.mytravelmemory.services

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ipca.mytravelmemory.models.TripModel
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

    fun getAll(
        userID: String,
        scope: CoroutineScope,
        callback: (ArrayList<TripModel>?) -> Unit,
    ) {
        scope.launch(Dispatchers.IO) {
            db.collection("users")
                .document(userID)
                .collection("trips")
                .get()
                .addOnSuccessListener { documents ->
                    scope.launch(Dispatchers.Main) {
                        val trips = arrayListOf<TripModel>()

                        if (documents == null) {
                            callback(null)
                            return@launch
                        }

                        for (document in documents) {
                            val trip = TripModel.convertToTripModel(document.data)
                            trips.add(trip)
                        }

                        // retornar resultado da operação
                        callback(trips)
                    }
                }
                .addOnFailureListener { error ->
                    scope.launch(Dispatchers.Main) {
                        callback(null)
                    }
                }
        }
    }
}