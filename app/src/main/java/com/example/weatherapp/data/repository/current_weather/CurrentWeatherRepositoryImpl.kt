package com.example.weatherapp.data.repository.current_weather

import com.example.weatherapp.data.datasource.current_weather.CurrentWeatherRemoteDataSourceImpl
import com.example.weatherapp.data.model.current_weather.CurrentWeather
import retrofit2.Response

class CurrentWeatherRepositoryImpl(private val currentWeatherRemoteDataSourceImpl: CurrentWeatherRemoteDataSourceImpl) :
    CurrentWeatherRepository {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Response<CurrentWeather> {

        return currentWeatherRemoteDataSourceImpl.getCurrentWeather(lat, lon, apiKey, units, lang)

    }
}