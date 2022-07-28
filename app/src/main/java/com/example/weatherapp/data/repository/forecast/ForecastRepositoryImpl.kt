package com.example.weatherapp.data.repository.forecast

import com.example.weatherapp.data.datasource.forecast.ForecastRemoteDataSourceImpl
import com.example.weatherapp.data.model.forecast.Forecast
import retrofit2.Response

class ForecastRepositoryImpl(private val forecastRemoteDataSourceImpl: ForecastRemoteDataSourceImpl) :
    ForecastRepository {
    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        cnt: Int,
        lang: String
    ): Response<Forecast> {

        return forecastRemoteDataSourceImpl.getForecast(lat, lon, apiKey, units, cnt, lang)

    }
}