package com.example.weatherapp.common

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun getCurrentDate(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy")
    return current.format(formatter)
}

fun getCurrentTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm a")
    return current.format(formatter).lowercase()
}

fun convertIntToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("hh:mm a")
    val output = format.format(date)
    Log.i("main_fragment", output.lowercase())
    return format.format(date)
}

fun String.toDate(dateFormat: String = "yyyy-MM-dd HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

// Description
/*broken clouds
* few clouds
* heavy intensity rain
* heavy snow
* light rain
* light snow
* moderate rain
* overcast clouds
* scattered clouds
* sky is clear
* snow
* very heavy rain*/