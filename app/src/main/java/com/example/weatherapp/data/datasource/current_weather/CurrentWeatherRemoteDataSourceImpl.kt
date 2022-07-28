package com.example.weatherapp.data.datasource.current_weather

import com.example.weatherapp.data.WeatherApi
import com.example.weatherapp.data.model.current_weather.CurrentWeather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class CurrentWeatherRemoteDataSourceImpl(
    private val api: WeatherApi,
    private val ioDispatcher: CoroutineDispatcher
) : CurrentWeatherRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Response<CurrentWeather> {
        return withContext(ioDispatcher) {
            api.weatherApiService.getCurrentWeather(lat, lon, apiKey, units, lang)
        }
    }
}