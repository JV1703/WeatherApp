package com.example.weatherapp.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val LAYOUT_PREFERENCES_NAME = "settings_preferences"
private val UNITS_SETTINGS = stringPreferencesKey("units_settings")
private val NOTIFICATION_SETTINGS = booleanPreferencesKey("notification_settings")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)

class SettingsDataStore(private val context: Context) {

    suspend fun saveUnitsToPreferenceStore(units: String) {
        context.dataStore.edit { preferences ->
            preferences[UNITS_SETTINGS] = units
        }
    }

    suspend fun saveNotificationToPreferenceStore(isNotification: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_SETTINGS] = isNotification
        }
    }

    val unitFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[UNITS_SETTINGS] ?: "metric"
        }

    val notificationFlow: Flow<Boolean> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[NOTIFICATION_SETTINGS] ?: true
        }
}