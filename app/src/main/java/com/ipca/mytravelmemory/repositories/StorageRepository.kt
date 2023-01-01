package com.ipca.mytravelmemory.repositories

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class StorageRepository {
    var storage = Firebase.storage

    /*
    fun upload(userID: String, tripID: String, photo: HashMap<String, Any?>): Task<Void> {
        var documentReference = db.collection("users")
            .document(userID)
            .collection("trips")
            .document(tripID)
            .collection("photos")
            .document()
        return documentReference.set(photo)
    }

    fun selectAll(userID: String): CollectionReference {
        val collectionReference = db.collection("users")
            .document(userID)
            .collection("trips")
        return collectionReference
    }
    */
}