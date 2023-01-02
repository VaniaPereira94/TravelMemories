package com.ipca.mytravelmemory.views.trip_detail

import androidx.lifecycle.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.TripRepository

class TripDetailViewModel : ViewModel() {
    private var result = MutableLiveData<Result<Boolean>>()

    private var tripRepository = TripRepository()
    private var authRepository = AuthRepository()

    fun removeTripFromFirebase(tripID: String, coverPath: String): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!

        tripRepository.delete(userID, tripID)
            .addOnSuccessListener {
                // remover foto da capa da viagem
                val storage = Firebase.storage
                val coverRef = storage.reference.child(coverPath)

                coverRef.delete()
                    .addOnFailureListener {
                        result.value = Result.failure(Throwable("Erro ao apagar viagem."))
                    }

                // remover fotos associadas à viagem
                val photosRef = storage.reference.child("${userID}/${tripID}/photos")

                photosRef.listAll()
                    .addOnSuccessListener { task ->
                        task.items.forEach { item ->
                            item.delete()
                        }
                        result.value = Result.success(true)
                    }
                    .addOnFailureListener {
                        result.value = Result.failure(Throwable("Erro ao apagar viagem."))
                    }
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao apagar viagem."))
            }

        return result
    }
}