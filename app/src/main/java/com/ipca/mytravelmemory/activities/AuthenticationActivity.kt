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

        // ao clicar no botão de iniciar sessão
        val buttonSignIn = findViewById<Button>(R.id.buttonSignIn)
        buttonSignIn.setOnClickListener {
            val editTextEmail = findViewById<EditText>(R.id.editTextEmailAuthentication)
            val editTextPassword = findViewById<EditText>(R.id.editTextPasswordAuthentication)

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // iniciar sessão com os dados inseridos pelo utilizador
            signIn(email, password)
        }

        // ao clicar no botão de criar conta
        val buttonSignUp = findViewById<Button>(R.id.buttonSignUpAuthentication)
        buttonSignUp.setOnClickListener {
            // ir para a página de criar conta
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // se iniciar sessão com sucesso
                if (task.isSuccessful) {
                    // ir para a página inicial
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                // se iniciar sessão falhou
                else {
                    // mostrar erro
                    Toast.makeText(baseContext, "Erro ao iniciar sessão.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}