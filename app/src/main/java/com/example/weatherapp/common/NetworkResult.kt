package com.example.weatherapp.common

import android.util.Log
import retrofit2.Response

sealed class NetworkResult<T>(
    val message: String? = null,
    val data: T? = null
) {

    class Success<T>(data: T) : NetworkResult<T>(data = data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(message, data)
    class Loading<T> : NetworkResult<T>()

}

fun <T : Any?> networkResultHandler(response: Response<T>): NetworkResult<T> {

    return when {
        response.message().toString().contains("timeout") -> {
            Log.i("network_result","timeout")
            NetworkResult.Error("Timeout")
        }
        response.code() == 402 -> {
            Log.i("network_result","402")
            NetworkResult.Error("API Key Limited")
        }
        response.isSuccessful -> {
            val response = response.body()
            Log.i("network_result","isSuccessful")
            NetworkResult.Success(response!!)
        }
        else -> {
            Log.i("network_result","isError")
            NetworkResult.Error("error code: ${response.code()}, error message:${response.message()}")
        }
    }

}