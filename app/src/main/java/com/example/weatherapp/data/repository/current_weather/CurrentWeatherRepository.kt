package com.example.weatherapp.data.repository.current_weather

import com.example.weatherapp.data.model.current_weather.CurrentWeather
import retrofit2.Response

interface CurrentWeatherRepository {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Response<CurrentWeather>

}