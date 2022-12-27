package com.ipca.mytravelmemory.fragments.trip

import androidx.lifecycle.*
import com.google.firebase.firestore.EventListener
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.repositories.TripRepository
import com.ipca.mytravelmemory.services.AuthService

class TripViewModel : ViewModel() {
    private var trips: MutableLiveData<List<TripModel>?> = MutableLiveData()
    private var errorMessage: MutableLiveData<String> = MutableLiveData()

    private var tripRepository = TripRepository()
    private var auth = AuthService()

    fun getTrips(): LiveData<List<TripModel>?> {
        val userID = auth.getUserID()

        tripRepository.selectAll(userID)
            .addSnapshotListener(EventListener { documents, event ->
                if (event != null) {
                    trips.value = null
                    return@EventListener
                }

                val tripsList: MutableList<TripModel> = mutableListOf()
                for (document in documents!!) {
                    val trip = TripModel.convertToTripModel(document.data)
                    tripsList.add(trip)
                }

                trips.value = tripsList
            })

        return trips
    }
}