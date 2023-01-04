package com.ipca.travelmemories.views.expense_create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ipca.travelmemories.models.ExpenseModel
import com.ipca.travelmemories.repositories.AuthRepository
import com.ipca.travelmemories.repositories.ExpenseRepository
import com.ipca.travelmemories.utils.ParserUtil

class ExpenseCreateViewModel : ViewModel() {
    private var result = MutableLiveData<Result<ExpenseModel>>()

    private var expenseRepository = ExpenseRepository()
    private var authRepository = AuthRepository()

    fun addExpensesToFirebase(
        tripID: String,
        category: String,
        price: Double,
        description: String,
        date: String
    ): LiveData<Result<ExpenseModel>> {
        // criar novo documento no firebase onde será guardada o nova dia do diário
        val userID = authRepository.getUserID()!!
        val documentReference = expenseRepository.setDocumentBeforeCreate(userID, tripID)

        // criar despesa
        val expenseID = documentReference.id
        val expense = ExpenseModel(
            expenseID,
            category,
            price,
            description,
            ParserUtil.convertStringToDate(date, "dd-MM-yyyy")
        )

        // adicionar à base de dados
        expenseRepository.create(documentReference, expense.convertToHashMap())
            .addOnSuccessListener {
                result.value = Result.success(expense)
            }
            .addOnFailureListener {
                result.value = Result.failure(Throwable("Erro ao adicionar uma despesa."))
            }

        return result
    }
}