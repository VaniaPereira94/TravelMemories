package com.ipca.mytravelmemory.views.profile

import androidx.lifecycle.*
import com.ipca.mytravelmemory.models.UserModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.UserRepository

class ProfileViewModel : ViewModel() {
    private var resultUser: MutableLiveData<Result<UserModel>> = MutableLiveData()
    private var resultStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()

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
                        resultUser.value = Result.success(user)
                    } else {
                        resultUser.value =
                            Result.failure(Throwable("Erro ao obter o utilizador autenticado."))
                    }
                }
                .addOnFailureListener {
                    resultUser.value =
                        Result.failure(Throwable("Erro ao obter o utilizador autenticado."))
                }
        }

        return resultUser
    }

    fun editUserDataFromFirebase(name: String, country: String?): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!

        if (name == "") {
            resultStatus.value =
                Result.failure(Throwable("O nome é obrigatório."))
        }

        val user = UserModel(name, country)

        userRepository.update(userID, user.convertToHashMap())
            .addOnSuccessListener {
                resultStatus.value = Result.success(true)
            }
            .addOnFailureListener {
                resultStatus.value =
                    Result.failure(Throwable("Erro ao atualizar dados do utilizador."))
            }

        return resultStatus
    }

    fun editUserEmailFromFirebase(email: String): LiveData<Result<Boolean>> {
        authRepository.updateEmail(email)
            .addOnSuccessListener {
                resultStatus.value = Result.success(true)
            }
            .addOnFailureListener {
                resultStatus.value =
                    Result.failure(Throwable("Erro ao atualizar dados do utilizador."))
            }

        return resultStatus
    }

    fun editUserPasswordFromFirebase(newPassword: String): LiveData<Result<Boolean>> {
        authRepository.updatePassword(newPassword)
            .addOnSuccessListener {
                resultStatus.value = Result.success(true)
            }
            .addOnFailureListener {
                resultStatus.value =
                    Result.failure(Throwable("Erro ao atualizar dados do utilizador."))
            }

        return resultStatus
    }

    fun removeUserFromFirebase(): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!

        authRepository.delete()
            .addOnSuccessListener {
                userRepository.delete(userID)
                    .addOnSuccessListener {
                        resultStatus.value = Result.success(true)
                    }
                    .addOnFailureListener {
                        resultStatus.value =
                            Result.failure(Throwable("Erro ao apagar conta do utilizador."))
                    }
            }
            .addOnFailureListener {
                resultStatus.value =
                    Result.failure(Throwable("Erro ao apagar conta do utilizador."))
            }

        return resultStatus
    }

    fun signOutFromFirebase() {
        authRepository.signOut()
    }
}