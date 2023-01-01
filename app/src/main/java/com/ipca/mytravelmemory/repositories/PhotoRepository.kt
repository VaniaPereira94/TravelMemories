package com.ipca.mytravelmemory.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class PhotoRepository {
    var db = FirebaseFirestore.getInstance()

    fun setDocumentBeforeCreate(userID: String, tripID: String): DocumentReference {
        var documentReference = db.collection("users")
            .document(userID)
            .collection("trips")
            .document(tripID)
            .collection("photos")
            .document()
        return documentReference
    }

    fun create(documentReference: DocumentReference, photo: HashMap<String, Any?>): Task<Void> {
        return documentReference.set(photo)
    }

    fun selectAll(userID: String, tripID: String): CollectionReference {
        var collectionReference = db.collection("users")
            .document(userID)
            .collection("trips")
            .document(tripID)
            .collection("photos")
        return collectionReference
    }
}