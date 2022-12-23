package com.ipca.mytravelmemory.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ipca.mytravelmemory.R

class TripDetailsActivity : AppCompatActivity() {
    var country: String? = null
    var city: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        country = intent.getStringExtra("country")
        city = intent.getStringExtra("city")

        findViewById<TextView>(R.id.textViewCountryName).text = country
        findViewById<TextView>(R.id.textViewCities).text = city
    }
}