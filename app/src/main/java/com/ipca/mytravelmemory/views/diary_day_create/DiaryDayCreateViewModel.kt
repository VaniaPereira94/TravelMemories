package com.ipca.mytravelmemory.views.diary_day_create

import androidx.lifecycle.*
import com.google.firebase.firestore.EventListener
import com.ipca.mytravelmemory.models.DiaryDayModel
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.DiaryDayRepository
import com.ipca.mytravelmemory.repositories.TripRepository
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
}