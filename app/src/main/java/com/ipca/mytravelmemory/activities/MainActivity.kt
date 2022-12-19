package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.ipca.mytravelmemory.R

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // ao clicar no botão de terminar sessão
        val buttonLogin = findViewById<Button>(R.id.buttonLogout)
        buttonLogin.setOnClickListener {
            // terminar sessão do utilizador
            logout()

            // ir para a página de autenticação
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logout() {
        auth.signOut()
    }
}