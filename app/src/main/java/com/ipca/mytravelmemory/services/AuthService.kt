package com.ipca.mytravelmemory.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthService {
    // acesso à autenticação
    private val auth = FirebaseAuth.getInstance()

    fun signUp(
        email: String,
        password: String,
        scope: CoroutineScope,
        callback: (Boolean) -> Unit,
    ) {
        scope.launch(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password)
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

    fun signIn(
        email: String,
        password: String,
        scope: CoroutineScope,
        callback: (Boolean) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
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

    fun signOut() {
        auth.signOut()
    }

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getUserID(): String {
        return auth.currentUser!!.uid
    }
}