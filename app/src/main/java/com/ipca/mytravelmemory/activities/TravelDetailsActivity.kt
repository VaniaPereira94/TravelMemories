package com.ipca.mytravelmemory.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ipca.mytravelmemory.R

class TravelDetailsActivity : AppCompatActivity() {
    var countryName: String? = null
    var cityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_details)

        countryName = intent.getStringExtra("country")
        cityName = intent.getStringExtra("city")

        findViewById<TextView>(R.id.textViewCountryName).text = countryName
        findViewById<TextView>(R.id.textViewCities).text = cityName
    }
}