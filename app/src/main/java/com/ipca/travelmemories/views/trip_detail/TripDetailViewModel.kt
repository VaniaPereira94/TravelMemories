package com.ipca.travelmemories.views.trip_detail

import androidx.lifecycle.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ipca.travelmemories.repositories.AuthRepository
import com.ipca.travelmemories.repositories.TripRepository

class TripDetailViewModel : ViewModel() {
    private var tripRepository = TripRepository()
    private var authRepository = AuthRepository()

    fun removeTripFromFirebase(
        tripID: String,
        coverPath: String,
        callback: (Result<Boolean>) -> Unit
    ) {
        val userID = authRepository.getUserID()!!

        tripRepository.delete(userID, tripID)
            .addOnSuccessListener {
                // remover ficheiro da foto da capa da viagem
                val storage = Firebase.storage
                val coverReference = storage.reference.child(coverPath)

                coverReference.delete()
                    .addOnFailureListener {
                        callback.invoke(Result.failure(Throwable("Erro ao apagar viagem.")))
                    }

                // remover ficheiros das fotos associadas à viagem
                val photosRef = storage.reference.child("${userID}/${tripID}/photos")

                photosRef.listAll()
                    .addOnSuccessListener { task ->
                        task.items.forEach { item ->
                            item.delete()
                        }
                        callback.invoke(Result.success(true))
                    }
                    .addOnFailureListener {
                        callback.invoke(Result.failure(Throwable("Erro ao apagar viagem.")))
                    }
            }
            .addOnFailureListener {
                callback.invoke(Result.failure(Throwable("Erro ao apagar viagem.")))
            }
    }
}