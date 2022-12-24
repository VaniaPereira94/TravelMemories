package com.ipca.mytravelmemory.models

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

class TripModel : Serializable {
    var country: String
    var cities: String? = null
    var startDate: Date
    var endDate: Date

    constructor(country: String, cities: String?, startDate: Date, endDate: Date) {
        this.country = country
        this.cities = cities
        this.startDate = startDate
        this.endDate = endDate
    }

    fun convertToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "country" to country,
            "cities" to cities,
            "startDate" to Timestamp(startDate),
            "endDate" to Timestamp(endDate)
        )
    }
}