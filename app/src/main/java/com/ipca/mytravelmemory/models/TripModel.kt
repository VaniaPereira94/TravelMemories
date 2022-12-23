package com.ipca.mytravelmemory.models

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

class TripModel : Serializable {
    var country: String
    var city: String? = null
    var startDate: Date
    var endDate: Date

    constructor(country: String, city: String?, startDate: Date, endDate: Date) {
        this.country = country
        this.city = city
        this.startDate = startDate
        this.endDate = endDate
    }

    fun convertToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "country" to country,
            "city" to city,
            "startDate" to Timestamp(startDate),
            "endDate" to Timestamp(endDate)
        )
    }
}