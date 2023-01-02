package com.ipca.mytravelmemory.views.photo_detail

import androidx.lifecycle.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PhotoDetailViewModel : ViewModel() {
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
}