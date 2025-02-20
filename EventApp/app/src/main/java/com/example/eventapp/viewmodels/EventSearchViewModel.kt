package com.example.eventapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import com.example.core.network.models.EventDto
import com.example.eventapp.navigation.components.Event
import com.example.ws.models.GetEventData
import com.example.ws.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EventSearchViewModel(private val repository: EventRepository) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<GetEventData>>(emptyList())
    val searchResults: StateFlow<List<GetEventData>> = _searchResults

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        searchEvents(newQuery)
    }

    private fun searchEvents(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchEvents(query)

                if (response.isSuccessful) {
                    _searchResults.value = response.body() ?: emptyList()
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }

}
