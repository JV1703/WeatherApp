package com.example.weatherapp.presentation.ui.fragment.settings_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.data_store.SettingsDataStore
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDataStore: SettingsDataStore) : ViewModel() {

    val savedUnits = settingsDataStore.unitFlow.asLiveData()
    val savedNotificationConfig = settingsDataStore.notificationFlow.asLiveData()

    fun saveUnits(units: String) {
        viewModelScope.launch {
            try {
                settingsDataStore.saveUnitsToPreferenceStore(units)
            } catch (e: Exception) {
                Log.d("settings_view_model", "saveUnits: ${e.message}")
            }
        }
    }

    fun saveNotificationConfig(isNotification: Boolean) {
        viewModelScope.launch {
            try {
                settingsDataStore.saveNotificationToPreferenceStore(isNotification)
            } catch (e: Exception) {
                Log.d("settings_view_model", "saveNotificationConfig: ${e.message}")
            }
        }
    }

}

class SettingsViewModelFactory(
    private val settingsDataStore: SettingsDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsDataStore) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}