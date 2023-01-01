package com.ipca.mytravelmemory.views.trip_create

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.TripRepository
import com.ipca.mytravelmemory.utils.ParserUtil
import java.io.File
import java.io.IOException
import java.util.*

class TripCreateViewModel : ViewModel() {
    private var result = MutableLiveData<Result<TripModel>>()

    private var tripRepository = TripRepository()
    private var authRepository = AuthRepository()

    lateinit var pathInDevice: String
    private lateinit var filename: String
    private lateinit var fullPath: String

    private fun setFullPath(userID: String, tripID: String): String {
        return "/${userID}/${tripID}/${filename}.jpg"
    }

    private fun getFullPath(): String {
        return fullPath
    }

    private fun setTrip(
        id: String,
        country: String,
        cities: String,
        startDate: String,
        endDate: String,
        coverPath: String
    ): TripModel {
        return TripModel(
            id,
            country,
            cities,
            ParserUtil.convertStringToDate(startDate, "dd-MM-yyyy"),
            ParserUtil.convertStringToDate(endDate, "dd-MM-yyyy"),
            coverPath
        )
    }

    fun addTripToFirebase(
        country: String,
        cities: String,
        startDate: String,
        endDate: String
    ): LiveData<Result<TripModel>> {
        // criar novo documento no firebase onde será guardada a nova viagem
        val userID = authRepository.getUserID()
        val documentReference = tripRepository.setDocumentBeforeCreate(userID)

        // criar viagem
        val tripID = documentReference.id
        fullPath = setFullPath(userID, tripID)
        val trip = setTrip(tripID, country, cities, startDate, endDate, fullPath)

        // adicionar à base de dados
        tripRepository.create(documentReference, trip.convertToHashMap())
            .addOnSuccessListener {
                result.value = Result.success(trip)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao adicionar viagem."))
            }

        return result
    }

    fun uploadFile(callback: (String?) -> Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val file = Uri.fromFile(File(pathInDevice))

        var metadata = storageMetadata {
            contentType = "image/jpg"
        }

        val ref = storageRef.child(getFullPath())
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
        filename = ParserUtil.convertDateToString(Date(), "yyyyMMdd_HHmmss")
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            filename, /* prefixo */
            ".jpg", /* sufixo */
            storageDir /* diretório */
        ).apply {
            // guardar um ficheiro: caminho para uso com intents ACTION_VIEW
            pathInDevice = this.absolutePath
        }
    }
}