package com.ipca.mytravelmemory.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

// para lidar com utilizadores na base de dados
class UserRepository {
    private var db = FirebaseFirestore.getInstance()

    fun create(userID: String, user: HashMap<String, Any?>): Task<Void> {
        val documentReference = db.collection("users")
            .document(userID)
        return documentReference.set(user)
    }

    fun selectOne(userID: String): Task<DocumentSnapshot> {
        val documentReference = db.collection("users")
            .document(userID)
        return documentReference.get()
    }

    fun updateData(userID: String, name: String, country: String?): Task<Void> {
        val documentReference = db.collection("users")
            .document(userID)
        return documentReference.update(
            mapOf(
                "name" to name,
                "country" to country
            )
        )
    }
}