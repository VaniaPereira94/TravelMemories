package com.ipca.mytravelmemory.utils

import java.text.SimpleDateFormat
import java.util.*

object ParserUtil {
    fun convertStringToDate(text: String, format: String): Date {
        return SimpleDateFormat(format).parse(text)
    }
}