package com.example.satelliteapp.util

data class GenericUIState<T> (
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)