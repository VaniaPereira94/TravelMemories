package com.ipca.mytravelmemory.views.expenses_all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ipca.mytravelmemory.models.ExpenseModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.ExpenseRepository

class ExpenseAllViewModel : ViewModel() {
    private var result: MutableLiveData<Result<List<ExpenseModel>>> = MutableLiveData()

    private var expenseRepository = ExpenseRepository()
    private var authRepository = AuthRepository()

    fun getExpensesFromFirebase(): LiveData<Result<List<ExpenseModel>>> {
        val userID = authRepository.getUserID()
        return result
    }
}