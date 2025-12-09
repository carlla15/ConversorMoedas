package com.example.currencyconverter.data.model

import java.util.Date

data class ConversionResult(
    val id: Long = 0,
    val fromCurrency: String,
    val toCurrency: String,
    val amount: Double,
    val convertedAmount: Double,
    val rate: Double,
    val timestamp: Date = Date(),
    val isDemo: Boolean = false
) {
    val formattedAmount: String get() = String.format("%.2f", amount)
    val formattedConvertedAmount: String get() = String.format("%.2f", convertedAmount)
    val formattedRate: String get() = String.format("%.6f", rate)

    fun toEntity(): com.example.currencyconverter.data.local.entity.ConversionEntity {
        return com.example.currencyconverter.data.local.entity.ConversionEntity(
            id = id,
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            amount = amount,
            convertedAmount = convertedAmount,
            rate = rate,
            timestamp = timestamp,
            isDemo = isDemo
        )
    }

    companion object {
        fun fromEntity(entity: com.example.currencyconverter.data.local.entity.ConversionEntity): ConversionResult {
            return ConversionResult(
                id = entity.id,
                fromCurrency = entity.fromCurrency,
                toCurrency = entity.toCurrency,
                amount = entity.amount,
                convertedAmount = entity.convertedAmount,
                rate = entity.rate,
                timestamp = entity.timestamp,
                isDemo = entity.isDemo
            )
        }
    }
}