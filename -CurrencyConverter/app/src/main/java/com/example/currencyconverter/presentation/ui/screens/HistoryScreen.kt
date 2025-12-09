package com.example.currencyconverter.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.currencyconverter.di.ViewModelFactory
import com.example.currencyconverter.presentation.viewmodel.CurrencyEvent
import com.example.currencyconverter.presentation.viewmodel.CurrencyViewModel
import com.example.currencyconverter.presentation.ui.components.HistoryItem
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: CurrencyViewModel = viewModel(factory = ViewModelFactory()),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "📊 Histórico de Conversões",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (state.conversionHistory.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onEvent(CurrencyEvent.ClearHistory) }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Limpar histórico")
                        }
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
            if (state.isLoadingHistory) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Carregando histórico...")
                }
            } else if (state.conversionHistory.isEmpty()) {
                EmptyHistoryState()
            } else {
                HistoryList(
                    conversions = state.conversionHistory,
                    onDelete = { id ->
                        viewModel.onEvent(CurrencyEvent.DeleteConversion(id))
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Diálogo de confirmação para limpar histórico
            if (state.showClearHistoryDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissClearHistoryDialog() },
                    title = { Text("Limpar Histórico") },
                    text = { Text("Tem certeza que deseja apagar todo o histórico de conversões? Esta ação não pode ser desfeita.") },
                    confirmButton = {
                        TextButton(
                            onClick = { viewModel.confirmClearHistory() }
                        ) {
                            Text("Limpar")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { viewModel.dismissClearHistoryDialog() }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HistoryList(
    conversions: List<com.example.currencyconverter.data.model.ConversionResult>,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(conversions) { conversion ->
            HistoryItem(
                conversion = conversion,
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun EmptyHistoryState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "Histórico vazio",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Nenhuma conversão salva",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "As conversões realizadas aparecerão aqui automaticamente",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}