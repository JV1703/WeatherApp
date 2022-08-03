package com.example.weatherapp.presentation.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.common.formatTo
import com.example.weatherapp.common.toDate
import com.example.weatherapp.data.model.forecast.Details
import com.example.weatherapp.databinding.ForecastViewHolderBinding
import kotlin.math.roundToInt

class ForecastAdapter : ListAdapter<Details, ForecastAdapter.ForecastViewHolder>(DiffCallback) {

    inner class ForecastViewHolder(private val binding: ForecastViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: Details) {
            binding.timeTv.text = forecast.dtTxt.toDate().formatTo("hh a").lowercase()
            binding.weatherIv.setImageResource(updateWeatherImage(forecast.weather.first().main))
            binding.temperatureTv.text = "${forecast.main.temp.roundToInt()}°"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ForecastViewHolder(ForecastViewHolderBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val currentForecast = getItem(position)
        holder.bind(currentForecast)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Details>() {
        override fun areItemsTheSame(oldItem: Details, newItem: Details): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Details, newItem: Details): Boolean {
            return oldItem == newItem
        }
    }

    private fun updateWeatherImage(weather: String): Int {
        return when (weather) {
            "Snow" -> R.drawable.weather_snow
            "Clear" -> R.drawable.sunny_main
            "Rain" -> R.drawable.weather_rain
            else -> R.drawable.weather_cloudy
        }
    }
}