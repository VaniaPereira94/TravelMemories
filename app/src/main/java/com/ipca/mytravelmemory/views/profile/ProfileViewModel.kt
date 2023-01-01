package com.ipca.mytravelmemory.views.profile

import androidx.lifecycle.*
import com.ipca.mytravelmemory.models.UserModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.UserRepository

class ProfileViewModel : ViewModel() {
    private var result: MutableLiveData<Result<UserModel>> = MutableLiveData()

    private var userRepository = UserRepository()
    private var authRepository = AuthRepository()

    fun getUserFromFirebase(): LiveData<Result<UserModel>> {
        val user = authRepository.getUser()

        user?.let {
            val userID = it.uid
            val email = it.email

            userRepository.selectOne(userID)
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = UserModel.convertToUserModel(userID, email!!, document.data!!)
                        result.value = Result.success(user)
                    } else {
                        result.value =
                            Result.failure(Throwable("Erro ao obter o utilizador autenticado."))
                    }
                }
                .addOnFailureListener {
                    result.value =
                        Result.failure(Throwable("Erro ao obter o utilizador autenticado."))
                }
        }

        return result
    }
}