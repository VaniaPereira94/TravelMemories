package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.ipca.mytravelmemory.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // iniciar thread secundária
        lifecycleScope.launch() {
            delay(4000)

            // quando concluir os 4 segundos
            launch {
                // acessar autenticação
                val auth = FirebaseAuth.getInstance()

                // se existe utilizador logado ao iniciar aplicação
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // ir para a página inicial
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)

                    // serve para que se o utilizador clicar na seta para voltar atrás, fechar aplicação
                    finish()
                } else {
                    // ir para a página de autenticação
                    val intent =
                        Intent(this@SplashScreenActivity, AuthenticationActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
        }
    }
}