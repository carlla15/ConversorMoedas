package com.example.currencyconverter.data.api.models

import com.example.currencyconverter.data.model.Currency

object AvailableCurrencies {
    val currencies = mapOf(
        // Moedas tradicionais
        "USD" to "Dólar Americano",
        "BRL" to "Real Brasileiro",
        "EUR" to "Euro",
        "GBP" to "Libra Esterlina",
        "JPY" to "Iene Japonês",
        "CAD" to "Dólar Canadense",
        "AUD" to "Dólar Australiano",
        "CHF" to "Franco Suíço",
        "CNY" to "Yuan Chinês",
        "ARS" to "Peso Argentino",

        // Criptomoedas
        "BTC" to "Bitcoin",
        "ETH" to "Ethereum",
        "LTC" to "Litecoin",
        "XRP" to "Ripple",
        "DOGE" to "Dogecoin"
    )

    fun getCurrencyList(): List<Currency> {
        return currencies.map { (code, name) ->
            Currency(code = code, name = name, flagEmoji = getFlagEmoji(code))
        }.sortedBy { it.code }
    }

    fun getFlagEmoji(code: String): String {
        return when (code) {
            "USD" -> "🇺🇸"
            "BRL" -> "🇧🇷"
            "EUR" -> "🇪🇺"
            "GBP" -> "🇬🇧"
            "JPY" -> "🇯🇵"
            "CAD" -> "🇨🇦"
            "AUD" -> "🇦🇺"
            "CHF" -> "🇨🇭"
            "CNY" -> "🇨🇳"
            "ARS" -> "🇦🇷"
            "BTC" -> "₿"
            "ETH" -> "Ξ"
            "LTC" -> "Ł"
            "XRP" -> "✕"
            "DOGE" -> "🐕"
            else -> "🏳️"
        }
    }}