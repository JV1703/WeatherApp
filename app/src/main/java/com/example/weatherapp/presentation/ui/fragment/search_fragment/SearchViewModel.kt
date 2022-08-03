package com.example.weatherapp.presentation.ui.fragment.search_fragment

import androidx.lifecycle.*
import com.example.weatherapp.common.NetworkResult
import com.example.weatherapp.common.networkResultHandler
import com.example.weatherapp.data.model.geoCoding.GeoCodingItem
import com.example.weatherapp.domain.geo_coding.GetGeoCodingUseCaseImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getGeoCodingUseCaseImpl: GetGeoCodingUseCaseImpl
) : ViewModel() {

    private val _geoCodingResponse = MutableLiveData<NetworkResult<List<GeoCodingItem>>>()
    val geoCodingResponse: LiveData<NetworkResult<List<GeoCodingItem>>> get() = _geoCodingResponse

    private var searchLocationJob: Job? = null

    fun searchLocation(q: String, limit: Int, appId: String) {
        searchLocationJob?.cancel()
        viewModelScope.launch {
            _geoCodingResponse.value = NetworkResult.Loading()
            try {
                val geoCodingResponse = getGeoCodingUseCaseImpl.searchLocation(q, limit, appId)
                _geoCodingResponse.value = networkResultHandler(geoCodingResponse)
            } catch (e: Exception) {
                _geoCodingResponse.value = NetworkResult.Error(e.message ?: "Something went wrong")
            }
        }
    }

}

class SearchViewModelFactory(
    private val getGeoCodingUseCaseImpl: GetGeoCodingUseCaseImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(getGeoCodingUseCaseImpl) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}