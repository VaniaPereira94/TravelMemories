package com.ipca.mytravelmemory.models

import com.google.firebase.Timestamp
import com.ipca.mytravelmemory.utils.ParserUtil
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

    companion object {
        fun convertToTripModel(hashMap: MutableMap<String, Any>): TripModel {
            return TripModel(
                hashMap["country"] as String,
                hashMap["cities"] as String?,
                ParserUtil.convertTimestampToString(hashMap["startDate"] as Timestamp),
                ParserUtil.convertTimestampToString(hashMap["endDate"] as Timestamp),
            )
        }
    }
}