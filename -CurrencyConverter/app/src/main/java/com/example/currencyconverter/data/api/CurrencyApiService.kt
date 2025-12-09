package com.example.currencyconverter.data.api

import com.example.currencyconverter.data.api.models.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {
    @GET("json/last/{from}-{to}")
    suspend fun getExchangeRate(
        @Path("from") from: String,
        @Path("to") to: String
    ): Map<String, ExchangeRateResponse>
}