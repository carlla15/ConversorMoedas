package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestManager(private val repository: CurrencyRepository) {

    private val _testResults = MutableStateFlow<List<String>>(emptyList())
    val testResults: StateFlow<List<String>> = _testResults

    suspend fun runAllTests() {
        val results = mutableListOf<String>()

        results.add("🚀 INICIANDO TESTES DE CONVERSÃO")
        results.add("=".repeat(50))

        // Teste 1: Conversões básicas
        results.add("📊 TESTE 1: Conversões básicas")
        testBasicConversions(results)

        // Teste 2: Conversões de criptomoedas
        results.add("\n💎 TESTE 2: Criptomoedas")
        testCryptocurrencies(results)

//        // Teste 3: Conversões múltiplas
//        results.add("\n🔄 TESTE 3: Conversões em lote")
//        val batchResults = repository.testMultipleConversions()
//        batchResults.forEach { result ->
//            results.add("${result.formattedAmount} ${result.fromCurrency} → ${result.formattedConvertedAmount} ${result.toCurrency}")
//        }

        // Teste 4: Histórico
        results.add("\n📈 TESTE 4: Verificando histórico")
        val count = repository.getConversionsCount()
        results.add("Total de conversões salvas: $count")

        _testResults.value = results
    }

    private suspend fun testBasicConversions(results: MutableList<String>) {
        val basicTests = listOf(
            Triple("USD", "BRL", 100.0),
            Triple("EUR", "BRL", 50.0),
            Triple("GBP", "USD", 75.0),
            Triple("JPY", "BRL", 10000.0),
            Triple("CAD", "BRL", 200.0),
            Triple("AUD", "USD", 150.0),
            Triple("ARS", "BRL", 10000.0),
            Triple("CHF", "EUR", 500.0),
            Triple("CNY", "USD", 1000.0)
        )

        for ((from, to, amount) in basicTests) {
            val response = repository.convertCurrency(from, to, amount)
            if (response.success) {
                val result = response.data as com.example.currencyconverter.data.model.ConversionResult
                results.add("✓ $amount $from → ${result.convertedAmount} $to (Taxa: ${result.formattedRate})")
            } else {
                results.add("✗ $from → $to: ${response.error}")
            }
        }
    }

    private suspend fun testCryptocurrencies(results: MutableList<String>) {
        val cryptoTests = listOf(
            Triple("BTC", "BRL", 0.5),
            Triple("ETH", "USD", 3.0),
            Triple("XRP", "BRL", 1000.0),
            Triple("DOGE", "USD", 50000.0),
            Triple("LTC", "BRL", 10.0),
            Triple("BTC", "ETH", 1.0)
        )

        for ((from, to, amount) in cryptoTests) {
            val response = repository.convertCurrency(from, to, amount)
            if (response.success) {
                val result = response.data as com.example.currencyconverter.data.model.ConversionResult
                results.add("✓ $amount $from → ${result.convertedAmount} $to")
            } else {
                results.add("✗ $from → $to: ${response.error}")
            }
        }
    }
}