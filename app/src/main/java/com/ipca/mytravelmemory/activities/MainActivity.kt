package com.ipca.mytravelmemory.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.models.TripModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    var trips = arrayListOf<TripModel>()
    val adapter = TipsAdapter()

    var resultLauncher: ActivityResultLauncher<Intent>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // ao clicar no botão de terminar sessão
        val buttonLogOut = findViewById<Button>(R.id.buttonLogOut)
        buttonLogOut.setOnClickListener {
            // terminar sessão do utilizador
            signOut()

            // ir para a página de autenticação
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        }

        val localDate = LocalDate.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val current = LocalDate.now().format(formatter)

        println("localDate: $localDate")
        println("current: $current")

        // lista das viagens
        val listViewTrips = findViewById<ListView>(R.id.listViewTrips)
        listViewTrips.adapter = adapter

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val countryName = it.data?.getStringExtra("country")
                val cityName = it.data?.getStringExtra("city")
                val startDate = it.data?.getStringExtra("startDate")
                val endDate = it.data?.getStringExtra("endDate")

                trips.add(
                    TripModel(
                        countryName ?: "",
                        cityName ?: "",
                        startDate ?: "",
                        endDate ?: ""
                    )
                )

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun signOut() {
        auth.signOut()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.buttonAdd -> {
                //add
                resultLauncher?.launch(Intent(this@MainActivity, TripCreateActivity::class.java))
                return true
            }
            else -> {
                return false
            }
        }
    }

    inner class TipsAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return trips.size
        }

        override fun getItem(position: Int): Any {
            return trips[position]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_trips, parent, false)

            val textViewName = rootView.findViewById<TextView>(R.id.textViewCountryMain)
            textViewName.text = trips[position].countryName

            rootView.setOnClickListener {
                val intent = Intent(this@MainActivity, TripDetailsActivity::class.java)
                intent.putExtra("country", trips[position].countryName)
                intent.putExtra("city", trips[position].cityName)
                startActivity(intent)
            }

            return rootView
        }
    }
}