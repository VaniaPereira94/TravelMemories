package com.ipca.mytravelmemory.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.services.TripService
import com.ipca.mytravelmemory.utils.ParserUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TripCreateActivity : AppCompatActivity() {
    private lateinit var trip: TripModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_create)

        // UI
        val editTextCountry = findViewById<EditText>(R.id.editTextCountry)
        val editTextCity = findViewById<EditText>(R.id.editTextCity)
        val textViewStartDate = findViewById<TextView>(R.id.textViewStartDate)
        val buttonStartDate = findViewById<Button>(R.id.buttonStartDate)
        val textViewEndDate = findViewById<TextView>(R.id.textViewEndDate)
        val buttonEndDate = findViewById<Button>(R.id.buttonEndDate)
        val buttonCreateTrip = findViewById<Button>(R.id.buttonCreateTrip)

        // dados dos selecionadores de datas
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // ao clicar no botão de selecionar data de início
        buttonStartDate.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    textViewStartDate.text = "$d-${m + 1}-$y"
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
                    textViewEndDate.text = "$d-${m + 1}-$y"
                },
                year,
                month,
                day
            ).show()
        }

        // ao clicar no botão de criar viagem
        buttonCreateTrip.setOnClickListener {
            // id do utilizador
            val auth = FirebaseAuth.getInstance()
            val userID = auth.currentUser!!.uid

            // definir viagem
            trip = TripModel(
                editTextCountry.text.toString(),
                editTextCity.text.toString(),
                ParserUtil.convertStringToDate(textViewStartDate.text.toString()),
                ParserUtil.convertStringToDate(textViewEndDate.text.toString())
            )

            lifecycleScope.launch(Dispatchers.IO) {
                // criar viagem na base de dados
                val tripService = TripService()
                var isCreated = tripService.create(userID, trip.convertToHashMap())

                // se viagem criada com sucesso
                if (isCreated) {
                    // enviar dados da viagem criada para a página principal
                    val intent = Intent()
                    intent.putExtra("EXTRA_TRIP", trip);

                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    // mostrar erro
                    Toast.makeText(baseContext, "Erro ao criar viagem.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}