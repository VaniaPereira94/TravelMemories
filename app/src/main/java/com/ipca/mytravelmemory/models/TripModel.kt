package com.ipca.mytravelmemory.models

class TripModel {
    var countryName: String? = null
    var cityName: String? = null
    var startDate: String? = null
    var endDate: String? = null

    constructor(countryName: String?, cityName: String?, startDate: String?, endDate: String?) {
        this.countryName = countryName
        this.cityName = cityName
        this.startDate = startDate
        this.endDate = endDate
    }
}