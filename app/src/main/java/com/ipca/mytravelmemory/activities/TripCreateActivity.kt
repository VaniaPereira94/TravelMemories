package com.ipca.mytravelmemory.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ipca.mytravelmemory.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class TripCreateActivity : AppCompatActivity() {
    var country: String? = null
    var city: String? = null
    var startDate: String? = null
    var endDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_create)

        country = intent.getStringExtra("country")
        city = intent.getStringExtra("city")
        startDate = intent.getStringExtra("startDate")
        endDate = intent.getStringExtra("endDate")

        // UI
        val editTextCountry = findViewById<EditText>(R.id.editTextCountry)
        val editTextCity = findViewById<EditText>(R.id.editTextCity)
        val textViewStartDate = findViewById<TextView>(R.id.textViewStartDate)
        val buttonStartDate = findViewById<Button>(R.id.buttonStartDate)
        val textViewEndDate = findViewById<TextView>(R.id.textViewEndDate)
        val buttonEndDate = findViewById<Button>(R.id.buttonEndDate)
        val buttonCreateTrip = findViewById<Button>(R.id.buttonCreateTrip)

        editTextCountry.setText(country)
        editTextCity.setText(city)
        textViewStartDate.text = startDate
        textViewEndDate.text = endDate

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // ao clicar no botão de selecionar data de início
        buttonStartDate.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    textViewStartDate.text = " $d-${m + 1}-$y "
                },
                year,
                month,
                day
            ).show()
        }

        // ao clicar no botão de selecionar data de fim
        buttonEndDate.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    textViewEndDate.text = " $d-${m + 1}-$y "
                },
                year,
                month,
                day
            ).show()
        }

        // ao clicar no botão de criar viagem
        buttonCreateTrip.setOnClickListener {
            // firebase
            val auth = FirebaseAuth.getInstance()
            val db = Firebase.firestore

            val userID = auth.currentUser!!.uid

            val startDateParse = SimpleDateFormat("dd-MM-yyyy").parse(textViewStartDate.text.toString())
            val endDateParse = SimpleDateFormat("dd-MM-yyyy").parse(textViewEndDate.text.toString())

            val trip = hashMapOf(
                "country" to editTextCountry.text.toString(),
                "city" to editTextCity.text.toString(),
                "startDate" to Timestamp(startDateParse),
                "endDate" to Timestamp(endDateParse)
            )

            db.collection("users")
                .document(userID)
                .collection("trips")
                .document()
                .set(trip)
                .addOnSuccessListener {
                    val intent = Intent()
                    intent.putExtra("country", editTextCountry.text.toString())
                    intent.putExtra("city", editTextCity.text.toString())
                    intent.putExtra("StartDate", textViewStartDate.text.toString())
                    intent.putExtra("EndDate", textViewEndDate.text.toString())

                    setResult(RESULT_OK, intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    print("Falha ao comunicar com o servidor.")
                }
        }
    }
}