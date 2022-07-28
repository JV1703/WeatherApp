package com.example.weatherapp.data.datasource.forecast

import com.example.weatherapp.data.WeatherApi
import com.example.weatherapp.data.model.forecast.Forecast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class ForecastRemoteDataSourceImpl(
    private val api: WeatherApi,
    private val ioDispatcher: CoroutineDispatcher
) : ForecastRemoteDataSource {
    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        cnt: Int,
        lang: String
    ): Response<Forecast> {
        return withContext(ioDispatcher) {
            api.weatherApiService.getForecast(lat, lon, apiKey, units, cnt, lang)
        }
    }
}