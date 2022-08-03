package com.example.weatherapp.data.model.current_weather


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "country")
    val country: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "sunrise")
    val sunrise: Long,
    @Json(name = "sunset")
    val sunset: Long,
    @Json(name = "type")
    val type: Int
)