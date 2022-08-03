package com.example.weatherapp.presentation.ui.fragment.main_fragment

import com.example.weatherapp.data.model.current_weather.CurrentWeather

data class WeatherUiState(
    val currentWeather: CurrentWeather? = null,
    val userMessages: List<String> = listOf(),
    val isLoading: Boolean = false
)