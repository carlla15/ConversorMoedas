package com.example.currencyconverter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.model.ConversionResult
import com.example.currencyconverter.domain.model.UiEvent
import com.example.currencyconverter.domain.repository.ICurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel

class CurrencyViewModel(
    private val repository: ICurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<UiEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        loadCurrencies()
        loadHistory()
    }

    fun onEvent(event: CurrencyEvent) {
        when (event) {
            CurrencyEvent.LoadCurrencies -> loadCurrencies()
            is CurrencyEvent.ChangeFromCurrency -> changeFromCurrency(event.currency)
            is CurrencyEvent.ChangeToCurrency -> changeToCurrency(event.currency)
            is CurrencyEvent.ChangeAmount -> changeAmount(event.amount)
            CurrencyEvent.ConvertCurrency -> convertCurrency()
            CurrencyEvent.SwapCurrencies -> swapCurrencies()
            CurrencyEvent.LoadHistory -> loadHistory()
            is CurrencyEvent.DeleteConversion -> deleteConversion(event.id)
            CurrencyEvent.ClearHistory -> showClearHistoryDialog()
            is CurrencyEvent.SelectConversion -> selectConversion(event.id)
            CurrencyEvent.ClearError -> clearError()
            CurrencyEvent.ClearConversionResult -> clearConversionResult()
        }
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCurrencies = true) }

            val response = repository.getAvailableCurrencies()

            if (response.success) {
                val currencies = response.data as? List<com.example.currencyconverter.data.model.Currency>
                    ?: emptyList()

                _uiState.update {
                    it.copy(
                        isLoadingCurrencies = false,
                        currencies = currencies,
                        error = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoadingCurrencies = false,
                        error = response.error
                    )
                }

                _eventChannel.send(UiEvent.ShowError(response.error ?: "Erro desconhecido"))
            }
        }
    }

    private fun convertCurrency() {
        val state = _uiState.value

        if (state.amount.isEmpty()) {
            viewModelScope.launch {
                _eventChannel.send(UiEvent.ShowError("Digite um valor"))
            }
            return
        }

        if (!state.isValidAmount) {
            viewModelScope.launch {
                _eventChannel.send(UiEvent.ShowError("Valor deve ser maior que zero"))
            }
            return
        }

        if (state.fromCurrency == state.toCurrency) {
            viewModelScope.launch {
                _eventChannel.send(UiEvent.ShowError("Selecione moedas diferentes"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingConversion = true, error = null) }

            val response = repository.convertCurrency(
                from = state.fromCurrency,
                to = state.toCurrency,
                amount = state.amountDouble
            )

            if (response.success) {
                val result = response.data as? ConversionResult

                _uiState.update {
                    it.copy(
                        isLoadingConversion = false,
                        conversionResult = result,
                        error = null
                    )
                }

                result?.let {
                    val message = "${it.formattedAmount} ${it.fromCurrency} = " +
                            "${it.formattedConvertedAmount} ${it.toCurrency}"
                    _eventChannel.send(UiEvent.ShowMessage(message))
                }

                loadHistory()

            } else {
                _uiState.update {
                    it.copy(
                        isLoadingConversion = false,
                        error = response.error
                    )
                }

                _eventChannel.send(UiEvent.ShowError(response.error ?: "Erro na conversão"))
            }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingHistory = true) }

            repository.getAllConversions().collectLatest { conversions ->
                _uiState.update {
                    it.copy(
                        isLoadingHistory = false,
                        conversionHistory = conversions
                    )
                }
            }
        }
    }

    private fun deleteConversion(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteConversion(id)
                _eventChannel.send(UiEvent.ShowMessage("Conversão excluída"))

                _uiState.update { state ->
                    state.copy(
                        conversionHistory = state.conversionHistory.filter { it.id != id }
                    )
                }
            } catch (e: Exception) {
                _eventChannel.send(UiEvent.ShowError("Erro ao excluir: ${e.message}"))
            }
        }
    }

    private fun showClearHistoryDialog() {
        _uiState.update { it.copy(showClearHistoryDialog = true) }
    }

    fun confirmClearHistory() {
        viewModelScope.launch {
            try {
                repository.clearAllConversions()
                _uiState.update { it.copy(conversionHistory = emptyList()) }
                _eventChannel.send(UiEvent.ShowMessage("Histórico limpo"))
            } catch (e: Exception) {
                _eventChannel.send(UiEvent.ShowError("Erro ao limpar histórico: ${e.message}"))
            } finally {
                _uiState.update { it.copy(showClearHistoryDialog = false) }
            }
        }
    }

    fun dismissClearHistoryDialog() {
        _uiState.update { it.copy(showClearHistoryDialog = false) }
    }

    private fun selectConversion(id: Long) {
        viewModelScope.launch {
            val conversion = repository.getConversionById(id)
            _uiState.update { it.copy(selectedConversion = conversion) }

            conversion?.let {
                _eventChannel.send(UiEvent.NavigateToConversion(it.id))
            }
        }
    }

    private fun changeFromCurrency(currency: String) {
        _uiState.update { it.copy(fromCurrency = currency) }
    }

    private fun changeToCurrency(currency: String) {
        _uiState.update { it.copy(toCurrency = currency) }
    }

    private fun changeAmount(amount: Double) {
        _uiState.update { it.copy(amount = amount.toString()) }
    }

    private fun swapCurrencies() {
        _uiState.update { state ->
            state.copy(
                fromCurrency = state.toCurrency,
                toCurrency = state.fromCurrency,
                conversionResult = null
            )
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun clearConversionResult() {
        _uiState.update { it.copy(conversionResult = null) }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun dismissError() {
        clearError()
    }
}