package com.example.core.network.models

import com.google.gson.annotations.SerializedName

data class SuccessfulResponseBody<T>(
    @SerializedName("data") val data: T?
)