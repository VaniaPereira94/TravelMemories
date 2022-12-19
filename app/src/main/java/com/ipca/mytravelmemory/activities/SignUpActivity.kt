package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ipca.mytravelmemory.R

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        // ao clicar no botão de criar conta
        val buttonSignUp = findViewById<Button>(R.id.buttonSignUp)
        buttonSignUp.setOnClickListener {
            val editTextEmail = findViewById<EditText>(R.id.editTextEmailSignUp)
            val editTextPassword = findViewById<EditText>(R.id.editTextPasswordSignUp)

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // criar conta com os dados inseridos pelo utilizador
            signUp(email, password)
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // se criar conta com sucesso
                if (task.isSuccessful) {
                    // ir para a página inicial
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                // se criar conta falhou
                else {
                    // mostrar erro
                    Toast.makeText(baseContext, "Erro ao criar conta.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }
}