package com.ipca.mytravelmemory.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class ExpenseRepository {
    private val db = FirebaseFirestore.getInstance()

    fun create(userID: String, tripID: String, expense: HashMap<String, Any?>): Task<Void> {
        val documentReference = db.collection("users")
            .document(userID)
            .collection("trips")
            .document(tripID)
            .collection("expenses")
            .document()
        return documentReference.set(expense)
    }

    fun selectAll(userID: String, tripID: String): CollectionReference {
        val collectionReference = db.collection("users")
            .document(userID)
            .collection("trips")
            .document(tripID)
            .collection("expenses")
        return collectionReference
    }

    fun delete(userID: String, tripID: String, expenseID: String): Task<Void> {
        val documentReference = db.collection("users")
            .document(userID)
            .collection("trips")
            .document(tripID)
            .collection("expenses")
            .document(expenseID)
        return documentReference.delete()
    }
}