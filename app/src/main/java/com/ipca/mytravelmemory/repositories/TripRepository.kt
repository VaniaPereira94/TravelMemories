package com.ipca.mytravelmemory.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class TripRepository {
    var db = FirebaseFirestore.getInstance()

    fun create(userID: String, trip: HashMap<String, Any?>): Task<Void> {
        var documentReference = db.collection("users")
            .document(userID)
            .collection("trips")
            .document()
        return documentReference.set(trip)
    }

    fun selectAll(userID: String): CollectionReference {
        val collectionReference = db.collection("users")
            .document(userID)
            .collection("trips")
        return collectionReference
    }
}