package com.ipca.mytravelmemory.views.diary_day_create

import androidx.lifecycle.*
import com.ipca.mytravelmemory.models.DiaryDayModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.DiaryDayRepository
import com.ipca.mytravelmemory.utils.ParserUtil

class DiaryDayCreateViewModel : ViewModel() {
    private var result: MutableLiveData<Result<Boolean>> = MutableLiveData()

    private var diaryDayRepository = DiaryDayRepository()
    private var authRepository = AuthRepository()

    private fun setDiaryDay(title: String?, body: String, date: String): DiaryDayModel {
        return DiaryDayModel(
            title,
            body,
            ParserUtil.convertStringToDate(date, "dd-MM-yyyy")
        )
    }

    fun addDiaryDayToFirebase(
        tripID: String,
        title: String?,
        body: String,
        date: String
    ): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!
        val diaryDay = setDiaryDay(title, body, date)

        diaryDayRepository.create(userID, tripID, diaryDay.convertToHashMap())
            .addOnSuccessListener {
                result.value = Result.success(true)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao adicionar dia ao diário."))
            }

        return result
    }
}