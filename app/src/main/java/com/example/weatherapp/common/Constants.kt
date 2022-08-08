package com.example.weatherapp.common

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = "1563ca049d1268cfa4b677a0c9c79f09"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    private val UNITS_SETTINGS = stringPreferencesKey("units_settings")
    private val NOTIFICATION_SETTINGS = booleanPreferencesKey("notification_settings")
}