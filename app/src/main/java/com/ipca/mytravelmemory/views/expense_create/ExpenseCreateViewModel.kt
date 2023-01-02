package com.ipca.mytravelmemory.views.expense_create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ipca.mytravelmemory.models.ExpenseModel
import com.ipca.mytravelmemory.repositories.AuthRepository
import com.ipca.mytravelmemory.repositories.ExpenseRepository
import com.ipca.mytravelmemory.utils.ParserUtil

class ExpenseCreateViewModel : ViewModel() {
    private var result = MutableLiveData<Result<ExpenseModel>>()

    private var expenseRepository = ExpenseRepository()
    private var authRepository = AuthRepository()

    private fun setExpense(category: String, price: Double, description: String, date: String): ExpenseModel {
        return ExpenseModel(
            category,
            price,
            description,
            ParserUtil.convertStringToDate(date, "dd-MM-yyyy")
        )
    }

    fun addExpensesToFirebase(
        tripID: String,
        category: String,
        price: Double,
        description: String,
        date: String
    ): LiveData<Result<ExpenseModel>> {
        val userID = authRepository.getUserID()!!
        val expense = setExpense(category, price, description, date)

       expenseRepository.create(userID, tripID, expense.convertToHashMap())
            .addOnSuccessListener {
                result.value = Result.success(expense)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao adicionar uma despesa."))
            }

        return result
    }
}