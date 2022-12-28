package com.ipca.mytravelmemory.views.home

import androidx.lifecycle.*
import com.google.firebase.firestore.EventListener
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.TripRepository

class HomeViewModel : ViewModel() {
    private var result: MutableLiveData<Result<List<TripModel>>> = MutableLiveData()

    private var tripRepository = TripRepository()
    private var authRepository = AuthRepository()

    fun getTripsFromFirebase(): LiveData<Result<List<TripModel>>> {
        val userID = authRepository.getUserID()

        tripRepository.selectAll(userID)
            .addSnapshotListener(EventListener { documents, error ->
                if (error != null) {
                    result.value = Result.failure(Throwable("Erro ao obter viagens."))
                    return@EventListener
                }

                val trips: MutableList<TripModel> = mutableListOf()
                for (document in documents!!) {
                    val trip = TripModel.convertToTripModel(document.data)
                    trips.add(trip)
                }

                result.value = Result.success(trips)
            })

        return result
    }

    fun signOutFromFirebase() {
        authRepository.signOut()
    }
}