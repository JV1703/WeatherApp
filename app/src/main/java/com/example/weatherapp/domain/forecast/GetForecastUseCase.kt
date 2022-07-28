package com.example.weatherapp.domain.forecast

import com.example.weatherapp.data.model.forecast.Forecast
import retrofit2.Response

interface GetForecastUseCase {

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        cnt: Int,
        lang: String
    ): Response<Forecast>

}