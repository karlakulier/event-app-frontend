package com.example.core.network

import com.example.core.network.models.ErrorResponseBody
import com.example.core.network.models.SuccessfulResponseBody

interface ResponseListener<T> {
    fun onSuccessfulResponse(response: T)
    fun onErrorResponse(response: ErrorResponseBody)
    fun onNetworkFailure(t: Throwable)
}