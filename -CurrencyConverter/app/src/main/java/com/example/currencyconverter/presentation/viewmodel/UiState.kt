package com.example.currencyconverter.presentation.viewmodel

import com.example.currencyconverter.data.model.ConversionResult
import com.example.currencyconverter.data.model.Currency

data class CurrencyUiState(
    // Loading States
    val isLoadingCurrencies: Boolean = true,
    val isLoadingConversion: Boolean = false,
    val isLoadingHistory: Boolean = false,

    // Data
    val currencies: List<Currency> = emptyList(),
    val fromCurrency: String = "USD",
    val toCurrency: String = "BRL",
    val amount: String = "1.0",
    val conversionResult: ConversionResult? = null,
    val conversionHistory: List<ConversionResult> = emptyList(),
    val selectedConversion: ConversionResult? = null,

    // UI State
    val error: String? = null,
    val showClearHistoryDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val conversionToDelete: Long? = null
) {
    val amountDouble: Double get() = amount.toDoubleOrNull() ?: 0.0
    val isValidAmount: Boolean get() = amountDouble > 0
    val canConvert: Boolean get() = fromCurrency.isNotEmpty() &&
            toCurrency.isNotEmpty() &&
            isValidAmount &&
            !isLoadingConversion

    val fromCurrencyDisplay: String get() = currencies.find { it.code == fromCurrency }?.name ?: fromCurrency
    val toCurrencyDisplay: String get() = currencies.find { it.code == toCurrency }?.name ?: toCurrency
}