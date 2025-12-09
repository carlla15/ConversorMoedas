package com.example.currencyconverter.data.model

data class ApiResponse(
    val success: Boolean = true,
    val data: Any? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)