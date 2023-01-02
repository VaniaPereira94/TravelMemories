package com.ipca.mytravelmemory.models

import com.google.firebase.Timestamp
import com.ipca.mytravelmemory.utils.ParserUtil
import java.io.Serializable
import java.util.*

class ExpenseModel : Serializable {
    var category: String? = null
    var price: Double? = null
    var description: String? = null
    var date: Date? = null

    constructor(category: String?, price: Double?, description: String?, date: Date?) {
        this.category = category
        this.price = price
        this.description = description
        this.date = date
    }

    // dados que serão armazenados no documento da base de dados pertencente à despesa
    fun convertToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "category" to category,
            "price" to price,
            "description" to description,
            "date" to Timestamp(date!!)
        )
    }

    companion object {
        // dados que serão recuperados da base de dados através do ID do documento e os dados dentro deste
        fun convertToExpenseModel(hashMap: MutableMap<String, Any>): ExpenseModel {
            return ExpenseModel(
                hashMap["category"] as String,
                hashMap["price"] as Double,
                hashMap["description"] as String?,
                ParserUtil.convertTimestampToString(hashMap["date"] as Timestamp),
            )
        }
    }
}