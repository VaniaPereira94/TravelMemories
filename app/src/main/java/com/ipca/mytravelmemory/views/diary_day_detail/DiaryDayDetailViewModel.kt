package com.ipca.mytravelmemory.views.diary_day_detail

import androidx.lifecycle.*
import com.ipca.mytravelmemory.models.DiaryDayModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.DiaryDayRepository
import com.ipca.mytravelmemory.utils.ParserUtil

class DiaryDayDetailViewModel : ViewModel() {
    private var result: MutableLiveData<Result<Boolean>> = MutableLiveData()

    private var diaryDayRepository = DiaryDayRepository()
    private var authRepository = AuthRepository()

    fun editDiaryDayFromFirebase(
        tripID: String,
        diaryDayID: String,
        title: String,
        body: String,
        date: String
    ): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!
        val diaryDay = DiaryDayModel(
            diaryDayID,
            title,
            body,
            ParserUtil.convertStringToDate(date, "dd-MM-yyyy")
        )

        diaryDayRepository.update(userID, tripID, diaryDayID, diaryDay.convertToHashMap())
            .addOnSuccessListener {
                result.value = Result.success(true)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao atualizar dados do utilizador."))
            }

        return result
    }

    fun removeDiaryDayFromFirebase(
        tripID: String,
        diaryDayID: String
    ): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!

        diaryDayRepository.delete(userID, tripID, diaryDayID)
            .addOnSuccessListener {
                result.value = Result.success(true)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao apagar dia do diário."))
            }

        return result
    }
}