package com.ipca.mytravelmemory.views.photo_create

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.ipca.mytravelmemory.models.PhotoModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.PhotoRepository
import com.ipca.mytravelmemory.utils.ParserUtil
import java.io.File
import java.io.IOException
import java.util.*

class PhotoCreateViewModel : ViewModel() {
    private var resultOnAdd: MutableLiveData<Result<Boolean>> = MutableLiveData()

    private var photoRepository = PhotoRepository()
    private var authRepository = AuthRepository()

    lateinit var currentPhotoPath: String

    fun uploadFile(currentPhotoPath: String, tripID: String, callback: (String?) -> Unit) {
        val userID = authRepository.getUserID()

        var storage = Firebase.storage
        val storageRef = storage.reference
        val file = Uri.fromFile(File(currentPhotoPath))

        var metadata = storageMetadata {
            contentType = "image/jpg"
        }

        val ref = storageRef.child("/${userID}/${tripID}/${file.lastPathSegment}")
        val uploadTask = ref.putFile(file)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback.invoke(ref.path)
            } else {
                callback.invoke(null)
            }
        }
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // nome do ficheiro
        val timeStamp: String = ParserUtil.convertDateToString(Date(), "yyyyMMdd_HHmmss")
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun setPhoto(path: String, description: String): PhotoModel {
        return PhotoModel(null, path, description)
    }

    fun addPhotoToDatabase(
        tripID: String,
        filePath: String,
        description: String
    ): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()
        val photo = setPhoto(filePath, description)

        photoRepository.create(userID, tripID, photo.convertToHashMap())
            .addOnSuccessListener {
                resultOnAdd.value = Result.success(true)
            }
            .addOnFailureListener {
                resultOnAdd.value = Result.failure(Throwable("Erro ao adicionar foto."))
            }

        return resultOnAdd
    }
}