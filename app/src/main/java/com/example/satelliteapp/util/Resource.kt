package com.example.satelliteapp.util

sealed class Resource<T>(
    val data: T? = null,
    val errorType: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(type: String?) : Resource<T>(errorType = type)
}