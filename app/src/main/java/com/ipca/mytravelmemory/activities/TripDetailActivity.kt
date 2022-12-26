package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.activities.MainActivity.Companion.EXTRA_TRIP_MAIN
import com.ipca.mytravelmemory.models.TripModel

class TripDetailActivity : AppCompatActivity() {
    private lateinit var trip: TripModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_detail)

        trip = intent.getSerializableExtra(EXTRA_TRIP_MAIN) as TripModel

        findViewById<TextView>(R.id.textViewCountry).text = trip.country
        findViewById<TextView>(R.id.textViewCities).text = trip.cities

        // ao clicar no botão de ir para o ecrã do diário
        val buttonClick = findViewById<Button>(R.id.buttonDiario)
        buttonClick.setOnClickListener {
            val intent = Intent(this@TripDetailActivity, DiaryDayAllActivity::class.java)
            startActivity(intent)
        }
    }
}