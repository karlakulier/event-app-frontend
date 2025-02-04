package com.example.eventapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ws.models.GetEventData
import com.example.ws.network.RetrofitClient.apiEvents
import com.example.ws.request_handlers.GetEventRequestHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetEventViewModel : ViewModel(){
    private val _events = MutableLiveData<List<GetEventData>>()
    val events: LiveData<List<GetEventData>> = _events

    private val eventRequestHandler = GetEventRequestHandler()

    fun fetchEvents() {
        val call = apiEvents.getActiveEvents()

        call.enqueue(object : Callback<List<GetEventData>> {
            override fun onResponse(
                call: Call<List<GetEventData>>,
                response: Response<List<GetEventData>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _events.postValue(response.body()!!)
                } else {
                    Log.e("GetEventViewModel", "Error: ${response.code()} - ${response.message()}")
                    _events.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<GetEventData>>, t: Throwable) {
                Log.e("GetEventViewModel", "Failed to fetch events", t)
                _events.postValue(emptyList())
            }
        })
    }


}