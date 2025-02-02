package com.example.core.network

interface RequestHandler<T> {
    fun sendRequest(responseListener: ResponseListener<T>)
}