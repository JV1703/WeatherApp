package com.example.weatherapp.data.repository.geo_coding

import com.example.weatherapp.data.datasource.geo_coding.GeoCodingRemoteDataSourceImpl
import com.example.weatherapp.data.model.geoCoding.GeoCodingItem
import retrofit2.Response

class GeoCodingRepositoryImpl(private val geoCodingRemoteDataSourceImpl: GeoCodingRemoteDataSourceImpl) :
    GeoCodingRepository {

    override suspend fun searchLocation(
        q: String,
        limit: Int,
        appId: String
    ): Response<List<GeoCodingItem>> {

        return geoCodingRemoteDataSourceImpl.searchLocation(q, limit, appId)

    }

}