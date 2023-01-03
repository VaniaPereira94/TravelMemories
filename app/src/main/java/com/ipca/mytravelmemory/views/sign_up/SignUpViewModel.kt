package com.ipca.mytravelmemory.views.sign_up

import androidx.lifecycle.*
import com.ipca.mytravelmemory.models.UserModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.UserRepository

class SignUpViewModel : ViewModel() {
    private val result = MutableLiveData<Result<Boolean>>()

    private var userRepository = UserRepository()
    private var authRepository = AuthRepository()

    private fun setUser(name: String): UserModel {
        return UserModel(name, "")
    }

    fun registerUserToFirebase(
        name: String,
        password: String,
        email: String
    ): (LiveData<Result<Boolean>>) {
        if (name == "" || email == "" || password == "") {
            result.value = Result.failure(Throwable("Campos vazios."))
            return result
        }

        // adicionar utilizador na autenticação do firebase
        authRepository.signUp(email, password)
            .addOnSuccessListener {
                val user = setUser(name)

                // adicionar utilizador na base de dados
                userRepository.create(authRepository.getUserID()!!, user.convertToHashMap())
                    .addOnSuccessListener {
                        result.value = Result.success(true)
                    }
                    .addOnFailureListener {
                        result.value = Result.failure(Throwable("Erro ao registar utilizador."))
                    }
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao registar utilizador."))
            }

        return result
    }
}