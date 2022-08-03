package com.example.weatherapp.presentation.ui.fragment.main_fragment

import com.example.weatherapp.data.model.forecast.Forecast

data class ForecastUiState(
    val forecasts: List<Forecast> = listOf(),
    val userMessages: List<String> = listOf()
)