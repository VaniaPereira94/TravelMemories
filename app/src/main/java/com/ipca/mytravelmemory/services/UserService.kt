package com.ipca.mytravelmemory.services

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ipca.mytravelmemory.models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserService {
    // acesso à base de dados
    private val db = Firebase.firestore

    fun create(
        userID: String,
        user: UserModel,
        scope: CoroutineScope,
        callback: (Boolean) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            db.collection("users")
                .document(userID)
                .set(user.convertToHashMap())
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
}