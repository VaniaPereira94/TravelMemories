package com.ipca.mytravelmemory.models

import com.google.firebase.Timestamp
import com.ipca.mytravelmemory.utils.ParserUtil
import java.io.Serializable
import java.util.*

class DiaryDayModel : Serializable {
    var title: String? = null
    var body: String
    var date: Date

    constructor(title: String?, body: String, date: Date) {
        this.title = title
        this.body = body
        this.date = date
    }

    fun convertToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "title" to title,
            "body" to body,
            "date" to Timestamp(Date())
        )
    }

    companion object {
        fun convertToDiaryDayModel(hashMap: MutableMap<String, Any>): DiaryDayModel {
            return DiaryDayModel(
                hashMap["title"] as String?,
                hashMap["body"] as String,
                ParserUtil.convertTimestampToString(hashMap["date"] as Timestamp),
            )
        }
    }
}