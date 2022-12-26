package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.models.UserModel
import com.ipca.mytravelmemory.services.AuthService
import com.ipca.mytravelmemory.services.UserService

class SignUpActivity : AppCompatActivity() {
    private lateinit var authService: AuthService
    private lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // ao clicar no botão de criar conta
        val buttonSignUp = findViewById<Button>(R.id.button_signUp_signUp)
        buttonSignUp.setOnClickListener {
            val editTextName = findViewById<EditText>(R.id.editText_signUp_name)
            val editTextEmail = findViewById<EditText>(R.id.editText_signUp_email)
            val editTextPassword = findViewById<EditText>(R.id.editText_signUp_password)

            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (name == "" && email == "" && password == "") {
                return@setOnClickListener
            }

            // criar conta com os dados inseridos pelo utilizador
            authService = AuthService()
            authService.signUp(email, password, lifecycleScope) { isRegistered ->
                if (!isRegistered) {
                    Toast.makeText(baseContext, "Erro ao criar conta.", Toast.LENGTH_SHORT)
                        .show()
                    return@signUp
                }

                // definir utilizador
                val user = UserModel(name, "")

                // adicionar utilizador na base de dados
                userService = UserService()
                userService.create(authService.getUserID(), user, lifecycleScope) { isCreated ->
                    if (!isCreated) {
                        // mostrar erro
                        Toast.makeText(
                            baseContext,
                            "Erro ao adicionar utilizador.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@create
                    }

                    // ir para a página principal
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}