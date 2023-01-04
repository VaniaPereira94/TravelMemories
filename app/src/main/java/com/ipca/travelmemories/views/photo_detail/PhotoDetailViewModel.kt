package com.ipca.travelmemories.views.photo_detail

import androidx.lifecycle.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ipca.travelmemories.repositories.AuthRepository
import com.ipca.travelmemories.repositories.PhotoRepository

class PhotoDetailViewModel : ViewModel() {
    private var result = MutableLiveData<Result<Boolean>>()

    private var photoRepository = PhotoRepository()
    private var authRepository = AuthRepository()

    fun getPhotoURI(filePath: String, callback: (Result<String>?) -> Unit) {
        val photoReference = Firebase.storage.reference.child(filePath)

        photoReference.downloadUrl
            .addOnSuccessListener { uri ->
                callback(Result.success(uri.toString()))
            }
            .addOnFailureListener {
                callback(Result.failure(Throwable("Erro ao visualizar foto.")))
            }
    }

    fun removePhotoFromFirebase(
        tripID: String,
        photoID: String,
        filePath: String
    ): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!

        photoRepository.delete(userID, tripID, photoID)
            .addOnSuccessListener {
                // remover ficheiro da foto
                val storage = Firebase.storage
                val fileReference = storage.reference.child(filePath)

                fileReference.delete()
                    .addOnSuccessListener {
                        result.value = Result.success(true)
                    }
                    .addOnFailureListener {
                        result.value = Result.failure(Throwable("Erro ao apagar foto."))
                    }
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao apagar foto."))
            }

        return result
    }
}