package com.example.weatherapp.presentation.ui.fragment.search_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.common.Constants
import com.example.weatherapp.common.NetworkResult
import com.example.weatherapp.data.WeatherApi
import com.example.weatherapp.data.datasource.geo_coding.GeoCodingRemoteDataSourceImpl
import com.example.weatherapp.data.repository.geo_coding.GeoCodingRepositoryImpl
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.domain.geo_coding.GetGeoCodingUseCaseImpl
import com.example.weatherapp.presentation.ui.adapter.SearchAdapter
import com.example.weatherapp.presentation.ui.common.DebouncingQueryTextListener
import kotlinx.coroutines.Dispatchers

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val geoCodingDataSourceImpl =
        GeoCodingRemoteDataSourceImpl(WeatherApi, Dispatchers.IO)
    private val geoCodingRepositoryImpl =
        GeoCodingRepositoryImpl(geoCodingDataSourceImpl)
    private val geoCodingUseCaseImpl =
        GetGeoCodingUseCaseImpl(geoCodingRepositoryImpl)

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(geoCodingUseCaseImpl)
    }

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = SearchAdapter { searchResult ->
            val action = SearchFragmentDirections.actionSearchFragmentToMainFragment(
                lat = searchResult.lat.toString(),
                lon = searchResult.lon.toString()
            )

            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView(lifecycle)
        setupSearchAdapter()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setupSearchView(lifecycle: Lifecycle) {
        binding.apply {
            search.setOnQueryTextListener(
                DebouncingQueryTextListener(lifecycle, 1000L) { query ->
                    if (query != null) {
                        searchLocation(query, appId = Constants.API_KEY)
                    }
                }
            )
            search.onActionViewExpanded()
        }
    }

    private fun setupSearchAdapter() {
        binding.searchResultRv.adapter = searchAdapter
    }

    private fun searchLocation(q: String, limit: Int = 5, appId: String) {
        viewModel.searchLocation(q, limit, appId)
        viewModel.geoCodingResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        Log.i("search_fragment", "Success: ${response.data}")
                        if (response.data.isEmpty()) {
                            binding.stateTv.text = "No results"
                            binding.stateTv.visibility = View.VISIBLE
                            binding.searchResultRv.visibility = View.GONE
                        } else {
                            searchAdapter.submitList(response.data)
                            binding.stateTv.visibility = View.GONE
                            binding.searchResultRv.visibility = View.VISIBLE
                        }
                    }
                }
                is NetworkResult.Error -> {
                    Log.i("search_fragment", "API call error: ${response.message}")
                    binding.stateTv.text = "No results"
                    binding.stateTv.visibility = View.VISIBLE
                    binding.searchResultRv.visibility = View.GONE
                }
                is NetworkResult.Loading -> {
                    binding.stateTv.text = "Searching..."
                    binding.stateTv.visibility = View.VISIBLE
                    binding.searchResultRv.visibility = View.GONE
                }
            }
        }
    }
}