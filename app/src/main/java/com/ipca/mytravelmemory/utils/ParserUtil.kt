package com.ipca.mytravelmemory.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object ParserUtil {
    fun convertStringToDate(text: String, format: String): Date {
        return SimpleDateFormat(format).parse(text)
    }

    fun convertTimestampToString(timestamp: Timestamp): Date {
        return timestamp.toDate()
    }
}