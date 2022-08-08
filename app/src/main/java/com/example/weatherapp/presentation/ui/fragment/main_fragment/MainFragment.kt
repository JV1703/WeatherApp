package com.example.weatherapp.presentation.ui.fragment.main_fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weatherapp.R
import com.example.weatherapp.common.Constants.API_KEY
import com.example.weatherapp.common.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.weatherapp.common.NetworkResult
import com.example.weatherapp.common.isOnline
import com.example.weatherapp.data.WeatherApi
import com.example.weatherapp.data.datasource.current_weather.CurrentWeatherRemoteDataSourceImpl
import com.example.weatherapp.data.datasource.forecast.ForecastRemoteDataSourceImpl
import com.example.weatherapp.data.repository.current_weather.CurrentWeatherRepositoryImpl
import com.example.weatherapp.data.repository.forecast.ForecastRepositoryImpl
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.domain.current_weather.GetCurrentWeatherUseCaseImpl
import com.example.weatherapp.domain.forecast.GetForecastUseCaseImpl
import com.example.weatherapp.presentation.ui.adapter.ForecastAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.math.roundToInt

class MainFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val currentWeatherRemoteDataSourceImpl =
        CurrentWeatherRemoteDataSourceImpl(WeatherApi, Dispatchers.IO)
    private val currentWeatherRepositoryImpl =
        CurrentWeatherRepositoryImpl(currentWeatherRemoteDataSourceImpl)
    private val getCurrentWeatherUseCaseImpl =
        GetCurrentWeatherUseCaseImpl(currentWeatherRepositoryImpl)

    private val forecastRemoteDataSourceImpl =
        ForecastRemoteDataSourceImpl(WeatherApi, Dispatchers.IO)
    private val forecastRepositoryImpl = ForecastRepositoryImpl(forecastRemoteDataSourceImpl)
    private val getForecastUseCaseImpl = GetForecastUseCaseImpl(forecastRepositoryImpl)

    private lateinit var forecastAdapter: ForecastAdapter

    private val navArgs: MainFragmentArgs by navArgs()

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(getForecastUseCaseImpl, getCurrentWeatherUseCaseImpl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("main_fragment", "onCreate: triggered")
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        forecastAdapter = ForecastAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("main_fragment", "onCreateView: triggered")
        getLocationAndWeather()
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("main_fragment", "onViewCreated: triggered")
        super.onViewCreated(view, savedInstanceState)
        setupRvAdapter()
        setupListeners()

        binding.refreshLayout.setOnRefreshListener {
            getLocationAndWeather()
            binding.refreshLayout.isRefreshing = false
        }

    }

    override fun onPause() {
        super.onPause()
        Log.i("main_fragment", "onPause: triggered")
    }

    override fun onStop() {
        super.onStop()
        Log.i("main_fragment", "onStop: triggered")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("main_fragment", "onDestroyView: triggered")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("main_fragment", "onDestroy: triggered")
        _binding = null
    }

    private fun hasLocationPermission() = EasyPermissions.hasPermissions(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private fun setupListeners() {

        binding.searchIv.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSearchFragment()
            findNavController().navigate(action)
        }

    }

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application requires location permission to work.",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Log.d("main_fragment", "onPermissionsDenied: $perms")
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permission granted!", Toast.LENGTH_SHORT).show()
    }

    private fun getLocationAndWeather() {
        if (hasLocationPermission()) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                Log.i("main_fragment", "lat: ${navArgs.lat} and lon: ${navArgs.lon}")
                if (navArgs.lat == null && navArgs.lon == null) {
                    getCurrentWeather(location.latitude, location.longitude, API_KEY)
                    getForecast(location.latitude, location.longitude, API_KEY)
                } else {
                    getCurrentWeather(navArgs.lat!!.toDouble(), navArgs.lon!!.toDouble(), API_KEY)
                    getForecast(navArgs.lat!!.toDouble(), navArgs.lon!!.toDouble(), API_KEY)
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String = "metric",
        lang: String = "en-us"
    ) {
        if (!isOnline(requireContext())) {
            binding.lottieNoConnection.visibility = View.VISIBLE
            binding.searchIv.isEnabled = false
        } else {
            binding.lottieNoConnection.visibility = View.GONE
            viewModel.getCurrentWeather(lat, lon, apiKey, units, lang)
            viewModel.currentWeatherResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        Log.i("main_fragment", "Success: ${response.data}")
                        response.data?.let {
                            binding.locationTv.text = it.name
                            binding.temperatureTv.text = "${it.main.temp.roundToInt()}°"
                            binding.weatherDescTv.text = it.weather.first().description
                            binding.weatherIv.setImageResource(updateWeatherImage(it.weather.first().main))
                            backgroundGradient(
                                it.sys.sunrise,
                                it.sys.sunset,
                                it.weather.first().main
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        binding.lottieNoConnection.visibility = View.VISIBLE
                        binding.searchIv.isEnabled = false
                        Log.i("main_fragment", "Error: ${response.message}")
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }
    }

    private fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String = "metric",
        cnt: Int = 40,
        lang: String = "en-us"
    ) {

        if (!isOnline(requireContext())) {
            binding.lottieNoConnection.visibility = View.VISIBLE
            binding.searchIv.isEnabled = false
        } else {
            binding.lottieNoConnection.visibility = View.GONE
            viewModel.getForecast(lat, lon, apiKey, units, cnt, lang)
            viewModel.forecastResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        Log.i("main_fragment", "Success: ${response.data}")
                        response.data?.let {
                            forecastAdapter.submitList(it.list)
                            Log.i("main_fragment", "forecast: ${it.list}")
                        }
                    }
                    is NetworkResult.Error -> {
                        binding.lottieNoConnection.visibility = View.VISIBLE
                        binding.searchIv.isEnabled = false
                        Log.i("main_fragment", "Error: ${response.message}")
                    }
                    is NetworkResult.Loading -> {}
                }
            }
        }

    }

    private fun setupRvAdapter() {
        binding.forecastRv.adapter = forecastAdapter
    }

    private fun updateWeatherImage(weather: String): Int {
        return when (weather) {
            "Snow" -> R.drawable.weather_snow
            "Clear" -> R.drawable.sunny_main
            "Rain" -> R.drawable.weather_rain
            else -> R.drawable.weather_cloudy
        }
    }

    private fun backgroundGradient(sunrise: Long, sunset: Long, weather: String) {
        val currentTime = Date().time / 1000
        when (weather) {
            "Rain", "Snow" -> {
                binding.root.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gradient_dark)
            }
            "Clouds" -> {
                if (currentTime in sunrise until sunset) {
                    binding.root.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.gradient_cloudy)
                } else {
                    binding.root.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.gradient_dark)
                }
            }
            "Clear" -> {
                if (currentTime in sunrise until sunset) {
                    binding.root.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.gradient_sunny)
                } else {
                    binding.root.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.gradient_dark)
                }
            }
        }
    }
}