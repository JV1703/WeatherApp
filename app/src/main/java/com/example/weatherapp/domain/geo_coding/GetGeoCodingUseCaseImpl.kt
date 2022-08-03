package com.example.weatherapp.domain.geo_coding

import com.example.weatherapp.data.model.geoCoding.GeoCodingItem
import com.example.weatherapp.data.repository.geo_coding.GeoCodingRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class GetGeoCodingUseCaseImpl(
    private val geoCodingRepositoryImpl: GeoCodingRepositoryImpl,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetGeoCodingUseCase {
    override suspend fun searchLocation(
        q: String,
        limit: Int,
        appId: String
    ): Response<List<GeoCodingItem>> {
        return withContext(defaultDispatcher) {
            geoCodingRepositoryImpl.searchLocation(q, limit, appId)
        }
    }
}