package com.ipca.mytravelmemory.views.splash_screen

import androidx.lifecycle.*
import com.ipca.mytravelmemory.repositories.AuthRepository

class SplashScreenViewModel : ViewModel() {
    private val result = MutableLiveData<Result<Boolean>>()

    private var authRepository = AuthRepository()

    fun isLoggedFromFirebase(): (LiveData<Result<Boolean>>) {
        val currentUser = authRepository.getUser()

        if (currentUser != null) {
            result.value = Result.success(true)
        } else {
            result.value = Result.failure(Throwable("Não existe utilizador logado."))
        }

        return result
    }
}