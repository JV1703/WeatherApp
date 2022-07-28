package com.example.weatherapp.domain.forecast

import com.example.weatherapp.data.model.forecast.Forecast
import com.example.weatherapp.data.repository.forecast.ForecastRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class GetForecastUseCaseImpl(
    private val getForecastRepositoryImpl: ForecastRepositoryImpl,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetForecastUseCase {

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        cnt: Int,
        lang: String
    ): Response<Forecast> {
        return withContext(defaultDispatcher) {
            getForecastRepositoryImpl.getForecast(lat, lon, apiKey, units, cnt, lang)
        }
    }

}