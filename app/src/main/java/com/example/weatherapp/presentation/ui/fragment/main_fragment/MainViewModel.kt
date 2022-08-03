package com.example.weatherapp.presentation.ui.fragment.main_fragment

import androidx.lifecycle.*
import com.example.weatherapp.common.NetworkResult
import com.example.weatherapp.common.networkResultHandler
import com.example.weatherapp.data.model.current_weather.CurrentWeather
import com.example.weatherapp.data.model.forecast.Forecast
import com.example.weatherapp.domain.current_weather.GetCurrentWeatherUseCaseImpl
import com.example.weatherapp.domain.forecast.GetForecastUseCaseImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val getForecastUseCaseImpl: GetForecastUseCaseImpl,
    private val getCurrentWeatherUseCaseImpl: GetCurrentWeatherUseCaseImpl
) : ViewModel() {

    private val _currentWeatherResponse = MutableLiveData<NetworkResult<CurrentWeather>>()
    val currentWeatherResponse: LiveData<NetworkResult<CurrentWeather>> get() = _currentWeatherResponse

    private val _forecastResponse = MutableLiveData<NetworkResult<Forecast>>()
    val forecastResponse: LiveData<NetworkResult<Forecast>> get() = _forecastResponse

    private var fetCurrentWeatherJob: Job? = null
    private var fetchForecastJob: Job? = null

    fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        cnt: Int,
        lang: String
    ) {
        fetchForecastJob?.cancel()
        fetchForecastJob = viewModelScope.launch {
            try {
                val forecastResponse =
                    getForecastUseCaseImpl.getForecast(lat, lon, apiKey, units, cnt, lang)

                _forecastResponse.value = networkResultHandler(forecastResponse)
            }catch (e: Exception){
                _currentWeatherResponse.value =
                    NetworkResult.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ) {
        fetCurrentWeatherJob?.cancel()
        fetCurrentWeatherJob = viewModelScope.launch {
            try {
                val currentWeatherResponse = getCurrentWeatherUseCaseImpl.getCurrentWeather(
                    lat,
                    lon,
                    apiKey,
                    units,
                    lang
                )

                _currentWeatherResponse.value = networkResultHandler(currentWeatherResponse)
            } catch (e: Exception) {
                _currentWeatherResponse.value =
                    NetworkResult.Error(e.message ?: "Something went wrong")
            }
        }
    }

}

class MainViewModelFactory(
    private val getForecastUseCaseImpl: GetForecastUseCaseImpl,
    private val getCurrentWeatherUseCaseImpl: GetCurrentWeatherUseCaseImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(getForecastUseCaseImpl, getCurrentWeatherUseCaseImpl) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}