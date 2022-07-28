package com.example.weatherapp.data.repository.geo_coding

import com.example.weatherapp.data.model.geoCoding.GeoCoding
import retrofit2.Response

interface GeoCodingRepository {
    suspend fun searchLocation(q: String, limit: Int, appId: String): Response<GeoCoding>
}