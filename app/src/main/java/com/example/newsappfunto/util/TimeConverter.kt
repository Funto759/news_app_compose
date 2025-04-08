package com.example.newsappfunto.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(dateString: String):String{
    return try {

        val zone = ZonedDateTime.parse(dateString)

        val format = DateTimeFormatter.ofPattern("MMMM d,yyyy", Locale.getDefault())

        zone.format(format)
    } catch (e:Exception){
        dateString
    }
}