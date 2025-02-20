package com.air.eventapplication.models

data class ApplicationGet(
    val id: Int,
    val status: String,
    val musicianId: Int,
    val musicianName: String,
    val musicianSurname: String,
)
