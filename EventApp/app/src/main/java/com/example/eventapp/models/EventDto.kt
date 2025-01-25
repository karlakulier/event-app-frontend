package com.example.eventapp.models

data class EventDto(   val dateTime: String,
                       val location: String,
                       val description: String,
                       val finished: Boolean,
                       val organizerId: Int
                    )
