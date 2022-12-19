package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ipca.mytravelmemory.R

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        // acessar autenticação
        auth = FirebaseAuth.getInstance()

        // ao clicar no botão de login
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
            val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // fazer login com os dados inseridos pelo utilizador
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // se login com sucesso
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    // ir para a página inicial
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                // se login falhou
                else {
                    // mostrar erro
                    Toast.makeText(baseContext, "Autenticação falhou.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}