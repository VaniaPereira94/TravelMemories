package com.ipca.mytravelmemory.views.trip_create

import androidx.lifecycle.*
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.TripRepository
import com.ipca.mytravelmemory.utils.ParserUtil

class TripCreateViewModel : ViewModel() {
    private var result = MutableLiveData<Result<TripModel>>()

    private var tripRepository = TripRepository()
    private var authRepository = AuthRepository()

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
}