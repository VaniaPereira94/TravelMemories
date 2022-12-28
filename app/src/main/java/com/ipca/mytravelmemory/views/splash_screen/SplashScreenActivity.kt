package com.ipca.mytravelmemory.views.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ipca.mytravelmemory.MainActivity
import com.ipca.mytravelmemory.databinding.ActivitySplashScreenBinding
import com.ipca.mytravelmemory.views.auth.AuthActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(3000)

            // após concluir 3 segundos
            launch {
                // verificar se existe utilizador logado ao iniciar aplicação
                viewModel.isLoggedFromFirebase().observe(this@SplashScreenActivity) { response ->
                    // ir para a página principal
                    response.onSuccess {
                        // ir para a página inicial
                        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                        startActivity(intent)

                        // serve para que se o utilizador clicar na seta para voltar atrás, fechar aplicação
                        finish()
                    }
                    // ir para a página de autenticação
                    response.onFailure {
                        val intent = Intent(this@SplashScreenActivity, AuthActivity::class.java)
                        startActivity(intent)

                        finish()
                    }
                }
            }
        }
    }
}