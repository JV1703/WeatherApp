package com.example.weatherapp.domain.geo_coding

import com.example.weatherapp.data.model.geoCoding.GeoCoding
import retrofit2.Response

interface GetGeoCodingUseCase {

    suspend fun searchLocation(q: String, limit: Int, appId: String): Response<GeoCoding>

}