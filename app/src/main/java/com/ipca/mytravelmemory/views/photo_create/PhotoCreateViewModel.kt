package com.ipca.mytravelmemory.views.photo_create

import androidx.lifecycle.*
import com.ipca.mytravelmemory.models.DiaryDayModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.PhotoRepository

class PhotoCreateViewModel : ViewModel() {
    private var result: MutableLiveData<Result<List<DiaryDayModel>>> = MutableLiveData()

    private var photoRepository = PhotoRepository()
    private var authRepository = AuthRepository()
}