package com.ipca.mytravelmemory.utils

import java.text.SimpleDateFormat
import java.util.*

object ParserUtil {
    fun convertStringToDate(text: String): Date {
        return SimpleDateFormat("dd-MM-yyyy").parse(text)
    }
}