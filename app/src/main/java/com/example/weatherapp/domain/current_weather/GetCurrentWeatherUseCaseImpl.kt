package com.example.weatherapp.domain.current_weather

import com.example.weatherapp.data.model.current_weather.CurrentWeather
import com.example.weatherapp.data.repository.current_weather.CurrentWeatherRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class GetCurrentWeatherUseCaseImpl(
    private val currentWeatherRepositoryImpl: CurrentWeatherRepositoryImpl,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetCurrentWeatherUseCase {

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Response<CurrentWeather> {
        return withContext(defaultDispatcher) {
            currentWeatherRepositoryImpl.getCurrentWeather(lat, lon, apiKey, units, lang)
        }
    }

}