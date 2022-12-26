package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.services.AuthService

class AuthActivity : AppCompatActivity() {
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // ao clicar no botão de iniciar sessão
        val buttonSignIn = findViewById<Button>(R.id.button_auth_signIn)
        buttonSignIn.setOnClickListener {
            val editTextEmail = findViewById<EditText>(R.id.editText_auth_email)
            val editTextPassword = findViewById<EditText>(R.id.editText_auth_password)

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // iniciar sessão do utilizador
            authService = AuthService()
            authService.signIn(email, password, lifecycleScope) { isLogged ->
                if (isLogged) {
                    // ir para a página inicial
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // mostrar erro
                    Toast.makeText(baseContext, "Erro ao iniciar sessão.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // ao clicar no botão de criar conta
        val buttonSignUp = findViewById<Button>(R.id.button_auth_signUp)
        buttonSignUp.setOnClickListener {
            // ir para a página de criar conta
            val intent = Intent(this@AuthActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}