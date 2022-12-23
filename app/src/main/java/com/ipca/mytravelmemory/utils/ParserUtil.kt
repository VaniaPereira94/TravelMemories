package com.ipca.mytravelmemory.utils

import java.text.SimpleDateFormat
import java.util.*

object ParserUtil {
    fun convertStringToDate(text: String): Date {
        return SimpleDateFormat("dd-MM-yyyy").parse(text)
    }

    fun convertDateToString(date: Date): String {
        """
        val localDate = LocalDate.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val current = LocalDate.now().format(formatter)

        return SimpleDateFormat("dd-MM-yyyy").parse(text)
        """
        return ""
    }
}