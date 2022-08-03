package com.example.weatherapp.data.datasource.geo_coding

import com.example.weatherapp.data.WeatherApi
import com.example.weatherapp.data.model.geoCoding.GeoCodingItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class GeoCodingRemoteDataSourceImpl(
    private val api: WeatherApi,
    private val ioDispatcher: CoroutineDispatcher
) : GeoCodingRemoteDataSource {

    override suspend fun searchLocation(
        q: String,
        limit: Int,
        appId: String
    ): Response<List<GeoCodingItem>> {
        return withContext(ioDispatcher) {
            api.weatherApiService.searchLocationWeather(q, limit, appId)
        }
    }

}