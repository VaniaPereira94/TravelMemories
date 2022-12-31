package com.ipca.mytravelmemory.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.HashMap

class PhotoRepository {
    var db = FirebaseFirestore.getInstance()
    var storage = Firebase.storage

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