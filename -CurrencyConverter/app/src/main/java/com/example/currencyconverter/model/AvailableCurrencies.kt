package com.example.currencyconverter.data.model

object AvailableCurrencies {
    val currencies = mapOf(
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
        "BTC" to "Bitcoin",
        "ETH" to "Ethereum",
        "LTC" to "Litecoin",
        "XRP" to "Ripple",
        "DOGE" to "Dogecoin"
    )

    fun getCurrencyList(): List<Currency> {
        return currencies.map { (code, name) ->
            Currency(code = code, name = name, flagEmoji = Currency.getFlagEmoji(code))
        }.sortedBy { it.code }
    }
}