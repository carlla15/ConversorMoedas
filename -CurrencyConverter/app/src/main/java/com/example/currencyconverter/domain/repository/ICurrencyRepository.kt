package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.data.model.ApiResponse
import com.example.currencyconverter.data.model.ConversionResult
import kotlinx.coroutines.flow.Flow

interface ICurrencyRepository {
    // API Operations
    suspend fun getAvailableCurrencies(): ApiResponse
    suspend fun convertCurrency(from: String, to: String, amount: Double): ApiResponse

    // Local Database Operations
    suspend fun saveConversion(conversion: ConversionResult): Long
    suspend fun updateConversion(conversion: ConversionResult)
    suspend fun deleteConversion(id: Long)
    suspend fun clearAllConversions()
    fun getAllConversions(): Flow<List<ConversionResult>>
    fun getConversionsByPair(from: String, to: String): Flow<List<ConversionResult>>
    suspend fun getConversionById(id: Long): ConversionResult?
    suspend fun getConversionsCount(): Int
}
