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
import com.google.firebase.auth.FirebaseAuth
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.models.TripModel

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    var travels = arrayListOf<TripModel>()
    val adapter = TravelsAdapter()

    var resultLauncher: ActivityResultLauncher<Intent>? = null

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

        // lista das viagens
        findViewById<ListView>(R.id.listViewTravels).adapter = adapter

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val countryName = it.data?.getStringExtra("countryName")
                val cityName = it.data?.getStringExtra("cityName")
                val startDate = it.data?.getStringExtra("StartDate")
                val endDate = it.data?.getStringExtra("EndDate")
                travels.add(
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
                resultLauncher?.launch(Intent(this@MainActivity, TravelCreateActivity::class.java))
                return true
            }
            else -> {
                return false
            }
        }
    }

    inner class TravelsAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return travels.size
        }

        override fun getItem(p0: Int): Any {
            return travels[p0]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_travel, p2, false)

            val textViewName = rootView.findViewById<TextView>(R.id.textViewCountryMain)

            textViewName.text = travels[p0].countryName

            rootView.setOnClickListener {
                val intent = Intent(this@MainActivity, TravelDetailsActivity::class.java)
                intent.putExtra("country", travels[p0].countryName)
                intent.putExtra("city", travels[p0].cityName)
                startActivity(intent)
            }

            return rootView
        }
    }
}