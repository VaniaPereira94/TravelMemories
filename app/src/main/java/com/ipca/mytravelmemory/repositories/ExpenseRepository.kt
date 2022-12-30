package com.ipca.mytravelmemory.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class ExpenseRepository {
    var db = FirebaseFirestore.getInstance()

    fun create(userID: String, expense: HashMap<String, Any?>): Task<Void> {
        var documentReference = db.collection("users")
            .document(userID)
            .collection("expenses")
            .document()
        return documentReference.set(expense)
    }
}