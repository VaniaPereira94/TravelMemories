package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ipca.mytravelmemory.R

class TravelCreateActivity : AppCompatActivity() {
    var countryName: String? = null
    var cityName: String? = null
    var startDate: String? = null
    var endDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_create)

        countryName = intent.getStringExtra("countryName")
        cityName = intent.getStringExtra("cityName")
        startDate = intent.getStringExtra("startDate")
        endDate = intent.getStringExtra("endDate")

        findViewById<EditText>(R.id.editTextCountry).setText(countryName)
        findViewById<EditText>(R.id.editTextCity).setText(cityName)
        findViewById<EditText>(R.id.editTextStart).setText(startDate)
        findViewById<EditText>(R.id.editTextEnd).setText(endDate)

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent()
            intent.putExtra(
                "countryName",
                findViewById<EditText>(R.id.editTextCountry).text.toString()
            )
            intent.putExtra("cityName", findViewById<EditText>(R.id.editTextCity).text.toString())
            intent.putExtra("StartDate", findViewById<EditText>(R.id.editTextStart).text.toString())
            intent.putExtra("EndDate", findViewById<EditText>(R.id.editTextEnd).text.toString())

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}