package com.example.weatherapp.data.model.forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Details (
    @Json(name = "clouds")
    val clouds: Clouds,
    @Json(name = "dt")
    val dt: Long,
    @Json(name = "dt_txt")
    val dtTxt: String,
    @Json(name = "main")
    val main: Main,
    @Json(name = "pop")
    val pop: Double,
    @Json(name = "sys")
    val sys: Sys,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "weather")
    val weather: List<Weather>,
    @Json(name = "wind")
    val wind: Wind
)