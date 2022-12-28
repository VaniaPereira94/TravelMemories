package com.ipca.mytravelmemory.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class UserRepository {
    var db = FirebaseFirestore.getInstance()

    fun create(userID: String, user: HashMap<String, Any?>): Task<Void> {
        var documentReference = db.collection("users")
            .document(userID)
        return documentReference.set(user)
    }
}