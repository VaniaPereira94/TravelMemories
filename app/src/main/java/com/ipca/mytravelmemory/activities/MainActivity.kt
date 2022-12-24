package com.ipca.mytravelmemory.activities

import android.app.Activity
import android.content.Intent
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
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.activities.TripCreateActivity.Companion.EXTRA_TRIP_CREATE
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.services.AuthService
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var authService: AuthService

    private var trips = arrayListOf<TripModel>()
    private val adapter = TipsAdapter()

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authService = AuthService()

        // ao clicar no botão de terminar sessão
        val buttonLogOut = findViewById<Button>(R.id.buttonLogOut)
        buttonLogOut.setOnClickListener {
            // terminar sessão do utilizador
            authService.signOut()

            // ir para a página de autenticação
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        trips.add(TripModel("País 1", "Cidade 1", Date(), Date()))
        trips.add(TripModel("País 2", "Cidade 2", Date(), Date()))

        // lista das viagens
        val listViewTrips = findViewById<ListView>(R.id.listViewTrips)
        listViewTrips.adapter = adapter

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val trip = it.data?.getSerializableExtra(EXTRA_TRIP_CREATE) as TripModel
                trips.add(trip)

                adapter.notifyDataSetChanged()
            }
        }
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

        override fun getItemId(position: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_trips, parent, false)

            val textViewName = rootView.findViewById<TextView>(R.id.textViewCountryMain)
            textViewName.text = trips[position].country

            rootView.setOnClickListener {
                val intent = Intent(this@MainActivity, TripDetailsActivity::class.java)
                intent.putExtra(EXTRA_TRIP_MAIN, trips[position])
                startActivity(intent)
            }

            return rootView
        }
    }

    companion object {
        const val EXTRA_TRIP_MAIN = "EXTRA_TRIP_MAIN"
    }
}