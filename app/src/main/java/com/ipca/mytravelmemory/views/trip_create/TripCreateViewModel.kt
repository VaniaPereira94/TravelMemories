package com.ipca.mytravelmemory.views.trip_create

import android.content.Context
import android.os.Environment
import androidx.lifecycle.*
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.TripRepository
import com.ipca.mytravelmemory.utils.ParserUtil
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TripCreateViewModel : ViewModel() {
    private var result = MutableLiveData<Result<TripModel>>()

    private var tripRepository = TripRepository()
    private var authRepository = AuthRepository()

     // var currentPhotoPath: MutableLiveData<String>()

    private fun setTrip(
        country: String,
        cities: String,
        startDate: String,
        endDate: String
    ): TripModel {
        return TripModel(
            null,
            country,
            cities,
            ParserUtil.convertStringToDate(startDate, "dd-MM-yyyy"),
            ParserUtil.convertStringToDate(endDate, "dd-MM-yyyy")
        )
    }

    fun addTripToFirebase(
        country: String,
        cities: String,
        startDate: String,
        endDate: String
    ): LiveData<Result<TripModel>> {
        val userID = authRepository.getUserID()
        val trip = setTrip(country, cities, startDate, endDate)

        tripRepository.create(userID, trip.convertToHashMap())
            .addOnSuccessListener {
                result.value = Result.success(trip)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao adicionar viagem."))
            }

        return result
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // nome do ficheiro
        val timeStamp: String = ParserUtil.convertDateToString(Date(), "dd-MM-yyyy")
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            timeStamp, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            //currentPhotoPath = absolutePath
        }
    }
}