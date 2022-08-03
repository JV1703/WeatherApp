package com.example.weatherapp.data

import com.example.weatherapp.data.model.current_weather.CurrentWeather
import com.example.weatherapp.data.model.forecast.Forecast
import com.example.weatherapp.data.model.geoCoding.GeoCoding
import com.example.weatherapp.data.model.geoCoding.GeoCodingItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Response<CurrentWeather>

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("cnt") cnt: Int /*max 40 timestamps*/,
        @Query("lang") lang: String
    ): Response<Forecast>

    @GET("geo/1.0/direct")
    suspend fun searchLocationWeather(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): Response<List<GeoCodingItem>>
}