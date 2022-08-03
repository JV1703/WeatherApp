package com.example.weatherapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.geoCoding.GeoCodingItem
import com.example.weatherapp.databinding.SearchResultViewHolderBinding

class SearchAdapter(private val clickListener: (GeoCodingItem) -> Unit) : ListAdapter<GeoCodingItem, SearchAdapter.SearchViewHolder>(DiffCallback) {
    class SearchViewHolder(private val binding: SearchResultViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: GeoCodingItem) {
            (binding.root as TextView).text =
                "${location.name}, ${location.state ?: location.name}, ${location.country}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SearchViewHolder(
            SearchResultViewHolderBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchResult = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener(searchResult)
        }
        holder.bind(searchResult)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<GeoCodingItem>() {
        override fun areItemsTheSame(oldItem: GeoCodingItem, newItem: GeoCodingItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GeoCodingItem, newItem: GeoCodingItem): Boolean {
            return oldItem == newItem
        }
    }
}