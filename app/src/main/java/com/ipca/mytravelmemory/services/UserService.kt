package com.ipca.mytravelmemory.services

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ipca.mytravelmemory.models.UserModel

class UserService {
    // acessar base de dados
    private val db = Firebase.firestore

    fun create(userID: String, user: UserModel): Boolean {
        var isCreated = false

        db.collection("users")
            .document(userID)
            .set(user.convertToHashMap())
            .addOnSuccessListener {
                isCreated = true
                print("SUCESSO")
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                isCreated = false
                print("ERRO")
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

        return isCreated
    }
}