package com.ipca.mytravelmemory.models

import com.google.firebase.Timestamp
import com.ipca.mytravelmemory.utils.ParserUtil
import java.io.Serializable
import java.util.*

class TripModel : Serializable {
    var id: String? = null
    var country: String? = null
    var cities: String? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var coverPath: String? = null

    constructor(
        id: String?,
        country: String?,
        cities: String?,
        startDate: Date?,
        endDate: Date?,
        coverPath: String?
    ) {
        this.id = id
        this.country = country
        this.cities = cities
        this.startDate = startDate
        this.endDate = endDate
        this.coverPath = coverPath
    }

    fun convertToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "country" to country,
            "cities" to cities,
            "startDate" to Timestamp(startDate!!),
            "endDate" to Timestamp(endDate!!),
            "coverPath" to coverPath!!
        )
    }

    companion object {
        fun convertToTripModel(id: String, hashMap: MutableMap<String, Any>): TripModel {
            return TripModel(
                id,
                hashMap["country"] as String,
                hashMap["cities"] as String?,
                ParserUtil.convertTimestampToString(hashMap["startDate"] as Timestamp),
                ParserUtil.convertTimestampToString(hashMap["endDate"] as Timestamp),
                hashMap["coverPath"] as String
            )
        }
    }
}