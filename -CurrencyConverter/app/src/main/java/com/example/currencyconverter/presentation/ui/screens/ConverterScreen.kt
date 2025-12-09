package com.example.currencyconverter.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.currencyconverter.di.ViewModelFactory
import com.example.currencyconverter.presentation.ui.components.ConversionResultCard
import com.example.currencyconverter.presentation.ui.components.CurrencyForm
import com.example.currencyconverter.presentation.viewmodel.CurrencyEvent
import com.example.currencyconverter.presentation.viewmodel.CurrencyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    navController: NavController,
    viewModel: CurrencyViewModel = viewModel(factory = ViewModelFactory()),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is com.example.currencyconverter.domain.model.UiEvent.ShowMessage -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                is com.example.currencyconverter.domain.model.UiEvent.ShowError -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.error,
                            duration = SnackbarDuration.Long
                        )
                    }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "💰 Conversor de Moedas",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screens.History.route) }
                    ) {
                        Icon(Icons.Default.History, contentDescription = "Histórico")
                    }

                    IconButton(
                        onClick = { viewModel.onEvent(CurrencyEvent.LoadCurrencies) },
                        enabled = !state.isLoadingCurrencies
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recarregar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoadingCurrencies) {
                LoadingState()
            } else if (state.error != null && state.currencies.isEmpty()) {
                ErrorState(
                    error = state.error!!,
                    onRetry = { viewModel.onEvent(CurrencyEvent.LoadCurrencies) }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CurrencyForm(
                        state = state,
                        onAmountChange = { viewModel.updateAmount(it) },
                        onFromCurrencyChange = { viewModel.onEvent(CurrencyEvent.ChangeFromCurrency(it)) },
                        onToCurrencyChange = { viewModel.onEvent(CurrencyEvent.ChangeToCurrency(it)) },
                        onSwapCurrencies = { viewModel.onEvent(CurrencyEvent.SwapCurrencies) },
                        onConvert = { viewModel.onEvent(CurrencyEvent.ConvertCurrency) },
                        isLoading = state.isLoadingConversion
                    )

                    state.conversionResult?.let { result ->
                        ConversionResultCard(
                            result = result,
                            onClear = { viewModel.onEvent(CurrencyEvent.ClearConversionResult) }
                        )
                    }

                    if (state.conversionHistory.isNotEmpty()) {
                        OutlinedButton(
                            onClick = { navController.navigate(Screens.History.route) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📊 Ver Histórico (${state.conversionHistory.size})")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = "Carregando moedas disponíveis...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ErrorState(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Erro",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Text(
                text = "Erro ao carregar",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )

            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Tentar Novamente")
            }
        }
    }
}