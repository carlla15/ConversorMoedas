package com.example.currencyconverter.presentation.viewmodel

sealed class CurrencyEvent {
    // Currency Events
    object LoadCurrencies : CurrencyEvent()
    data class ChangeFromCurrency(val currency: String) : CurrencyEvent()
    data class ChangeToCurrency(val currency: String) : CurrencyEvent()
    data class ChangeAmount(val amount: Double) : CurrencyEvent()
    object ConvertCurrency : CurrencyEvent()
    object SwapCurrencies : CurrencyEvent()

    // History Events
    object LoadHistory : CurrencyEvent()
    data class DeleteConversion(val id: Long) : CurrencyEvent()
    object ClearHistory : CurrencyEvent()
    data class SelectConversion(val id: Long) : CurrencyEvent()

    // UI Events
    object ClearError : CurrencyEvent()
    object ClearConversionResult : CurrencyEvent()
}