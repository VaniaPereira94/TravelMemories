package com.ipca.travelmemories.views.expense_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ipca.travelmemories.models.ExpenseModel
import com.ipca.travelmemories.repositories.AuthRepository
import com.ipca.travelmemories.repositories.ExpenseRepository
import com.ipca.travelmemories.utils.ParserUtil

class ExpenseDetailViewModel : ViewModel() {
    private var result: MutableLiveData<Result<Boolean>> = MutableLiveData()

    private var expenseRepository = ExpenseRepository()
    private var authRepository = AuthRepository()

    fun editExpenseFromFirebase(
        tripID: String,
        expenseID: String,
        category: String,
        price: Double,
        description: String,
        date: String
    ): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!
        val expense = ExpenseModel(
            expenseID,
            category,
            price,
            description,
            ParserUtil.convertStringToDate(date, "dd-MM-yyyy")
        )

        expenseRepository.update(userID, tripID, expenseID, expense.convertToHashMap())
            .addOnSuccessListener {
                result.value = Result.success(true)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao atualizar dados do utilizador."))
            }

        return result
    }

    fun removeExpenseFromFirebase(
        tripID: String,
        expenseID: String
    ): LiveData<Result<Boolean>> {
        val userID = authRepository.getUserID()!!

        expenseRepository.delete(userID, tripID, expenseID)
            .addOnSuccessListener {
                result.value = Result.success(true)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao apagar dia do diário."))
            }

        return result
    }
}