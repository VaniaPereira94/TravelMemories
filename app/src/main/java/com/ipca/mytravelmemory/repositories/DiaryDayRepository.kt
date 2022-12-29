package com.ipca.mytravelmemory.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class DiaryDayRepository {
    var db = FirebaseFirestore.getInstance()

    fun create(userID: String, tripID: String, trip: HashMap<String, Any?>): Task<Void> {
        var documentReference = db.collection("users")
            .document(userID)
            .collection("trips")
            .document(tripID)
            .collection("diary")
            .document()
        return documentReference.set(trip)
    }
}