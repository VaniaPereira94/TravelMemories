package com.ipca.mytravelmemory.views.home

import androidx.lifecycle.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ipca.mytravelmemory.models.DeviceModel
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.NotificationTokenRepository
import com.ipca.mytravelmemory.repositories.TripRepository
import com.ipca.mytravelmemory.utils.ParserUtil

class HomeViewModel : ViewModel() {
    private var resultTrips: MutableLiveData<Result<List<TripModel>>> = MutableLiveData()
    private var resultStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()

    private var tripRepository = TripRepository()
    private var authRepository = AuthRepository()
    private var notificationTokenRepository = NotificationTokenRepository()

    fun getTripsFromFirebase(): LiveData<Result<List<TripModel>>> {
        val userID = authRepository.getUserID()!!

        tripRepository.selectAll(userID)
            .addSnapshotListener(EventListener { documents, error ->
                if (error != null) {
                    resultTrips.value = Result.failure(Throwable("Erro ao obter viagens."))
                    return@EventListener
                }

                val trips: MutableList<TripModel> = mutableListOf()
                for (document in documents!!) {
                    val trip = TripModel.convertToTripModel(document.id, document.data)
                    trips.add(trip)
                }

                resultTrips.value = Result.success(trips)
            })

        return resultTrips
    }

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

    fun getDeviceToken(): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!

        val device = DeviceModel(null, "coverPath")

        // adicionar à base de dados
        notificationTokenRepository.create(userID, device.convertToHashMap())
            .addOnSuccessListener {
                resultStatus.value = Result.success(true)
            }
            .addOnFailureListener {
                resultStatus.value = Result.failure(Throwable("Erro ao adicionar viagem."))
            }

        return resultStatus
    }

    fun signOutFromFirebase() {
        authRepository.signOut()
    }
}