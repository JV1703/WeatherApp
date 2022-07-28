package com.example.weatherapp.data.datasource.forecast

import com.example.weatherapp.data.model.forecast.Forecast
import retrofit2.Response

interface ForecastRemoteDataSource {

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        cnt: Int,
        lang: String
    ): Response<Forecast>

}