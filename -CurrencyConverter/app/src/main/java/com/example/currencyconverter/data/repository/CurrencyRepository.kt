package com.example.currencyconverter.data.repository

import android.content.Context
import com.example.currencyconverter.data.local.database.AppDatabase
import com.example.currencyconverter.data.model.ApiResponse
import com.example.currencyconverter.data.model.AvailableCurrencies
import com.example.currencyconverter.data.model.ConversionResult
import com.example.currencyconverter.data.model.Currency
import com.example.currencyconverter.domain.repository.ICurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import kotlin.math.round

class CurrencyRepository(
    private val context: Context,
    private val database: AppDatabase
) : ICurrencyRepository {

    private val conversionDao = database.conversionDao()

    private val usdBasedRates = mapOf(
        // Taxas em relação ao USD (1 USD = X moeda)
        "USD" to 1.0,      // USD para USD
        "BRL" to 5.05,    // 1 USD = 5.05 BRL
        "EUR" to 0.92,    // 1 USD = 0.92 EUR
        "GBP" to 0.79,    // 1 USD = 0.79 GBP
        "JPY" to 147.5,   // 1 USD = 147.5 JPY
        "CAD" to 1.35,    // 1 USD = 1.35 CAD
        "AUD" to 1.52,    // 1 USD = 1.52 AUD
        "CHF" to 0.88,    // 1 USD = 0.88 CHF
        "CNY" to 7.18,    // 1 USD = 7.18 CNY
        "ARS" to 850.0,   // 1 USD = 850 ARS

        // Criptomoedas (em USD)
        "BTC" to 0.000022, // 1 USD = 0.000022 BTC (1 BTC = ~45000 USD)
        "ETH" to 0.0004,   // 1 USD = 0.0004 ETH (1 ETH = ~2500 USD)
        "LTC" to 0.0139,   // 1 USD = 0.0139 LTC (1 LTC = ~72 USD)
        "XRP" to 1.6129,   // 1 USD = 1.6129 XRP (1 XRP = ~0.62 USD)
        "DOGE" to 6.6667   // 1 USD = 6.6667 DOGE (1 DOGE = ~0.15 USD)
    )

    override suspend fun getAvailableCurrencies(): ApiResponse {
        return try {
            ApiResponse(
                success = true,
                data = AvailableCurrencies.getCurrencyList()
            )
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = "Erro ao carregar moedas: ${e.message}"
            )
        }
    }

    override suspend fun convertCurrency(from: String, to: String, amount: Double): ApiResponse {
        return try {
             if (from == to) {
                return ApiResponse(
                    success = false,
                    error = "Selecione moedas diferentes"
                )
            }


            if (amount <= 0) {
                return ApiResponse(
                    success = false,
                    error = "Digite um valor maior que zero"
                )
            }


            val rate = calculateExchangeRate(from, to)

            if (rate == 0.0) {
                return ApiResponse(
                    success = false,
                    error = "Taxa de câmbio não disponível para $from → $to"
                )
            }


            val convertedAmount = round(amount * rate * 100) / 100


            val result = ConversionResult(
                fromCurrency = from,
                toCurrency = to,
                amount = amount,
                convertedAmount = convertedAmount,
                rate = rate,
                timestamp = Date(),
                isDemo = true
            )


            val id = saveConversion(result)
            val savedResult = result.copy(id = id)

            ApiResponse(
                success = true,
                data = savedResult
            )

        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = "Erro na conversão: ${e.message}"
            )
        }
    }

    private fun calculateExchangeRate(from: String, to: String): Double {

        val fromRate = usdBasedRates[from]
        val toRate = usdBasedRates[to]

        if (fromRate != null && toRate != null) {

            return fromRate / toRate
        }


        val inverseRate = calculateExchangeRate(to, from)
        if (inverseRate > 0) {
            return 1.0 / inverseRate
        }

        return 0.0
    }


    fun debugAllRates(): Map<String, Double> {
        val allRates = mutableMapOf<String, Double>()

        val currencies = usdBasedRates.keys.toList()

        for (from in currencies) {
            for (to in currencies) {
                if (from != to) {
                    val rate = calculateExchangeRate(from, to)
                    if (rate > 0) {
                        allRates["${from}${to}"] = rate
                    }
                }
            }
        }

        return allRates
    }


    override suspend fun saveConversion(conversion: ConversionResult): Long {
        return conversionDao.insertConversion(conversion.toEntity())
    }

    override suspend fun updateConversion(conversion: ConversionResult) {
        conversionDao.updateConversion(conversion.toEntity())
    }

    override suspend fun deleteConversion(id: Long) {
        conversionDao.deleteConversionById(id)
    }

    override suspend fun clearAllConversions() {
        conversionDao.clearAllConversions()
    }

    override fun getAllConversions(): Flow<List<ConversionResult>> {
        return conversionDao.getAllConversions()
            .map { entities -> entities.map { ConversionResult.fromEntity(it) } }
    }

    override fun getConversionsByPair(from: String, to: String): Flow<List<ConversionResult>> {
        return conversionDao.getConversionsByPair(from, to)
            .map { entities -> entities.map { ConversionResult.fromEntity(it) } }
    }

    override suspend fun getConversionById(id: Long): ConversionResult? {
        return conversionDao.getConversionById(id)?.let { ConversionResult.fromEntity(it) }
    }

    override suspend fun getConversionsCount(): Int {
        return conversionDao.getConversionsCount()
    }
}
