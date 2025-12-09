package com.example.currencyconverter.data.model

@kotlinx.serialization.Serializable
data class Currency(
    val code: String,
    val name: String,
    val flagEmoji: String = ""
) {
    companion object {
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
                "XRP" -> "XRP"
                else -> "🏳️"
            }
        }
    }
}