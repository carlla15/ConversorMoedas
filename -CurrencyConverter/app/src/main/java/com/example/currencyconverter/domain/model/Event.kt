package com.example.currencyconverter.domain.model

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    data class ShowError(val error: String) : UiEvent()
    object NavigateToHistory : UiEvent()
    object NavigateBack : UiEvent()
    data class NavigateToConversion(val conversionId: Long) : UiEvent()
    object ClearHistory : UiEvent()
}