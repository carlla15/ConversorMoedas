package com.example.currencyconverter.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.presentation.viewmodel.CurrencyUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyForm(
    state: CurrencyUiState,
    onAmountChange: (String) -> Unit,
    onFromCurrencyChange: (String) -> Unit,
    onToCurrencyChange: (String) -> Unit,
    onSwapCurrencies: () -> Unit,
    onConvert: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Text(
                text = "Converter Moeda",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Campo de valor
            OutlinedTextField(
                value = state.amount,
                onValueChange = onAmountChange,
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = !state.isValidAmount && state.amount.isNotEmpty(),
                supportingText = {
                    if (!state.isValidAmount && state.amount.isNotEmpty()) {
                        Text("Valor deve ser maior que zero")
                    }
                }
            )

            // Moeda de origem
            var expandedFrom by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedFrom,
                onExpandedChange = { expandedFrom = it }
            ) {
                OutlinedTextField(
                    value = state.fromCurrency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("De") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFrom)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedFrom,
                    onDismissRequest = { expandedFrom = false }
                ) {
                    state.currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = {
                                Text("${currency.code} - ${currency.name}")
                            },
                            onClick = {
                                onFromCurrencyChange(currency.code)
                                expandedFrom = false
                            }
                        )
                    }
                }
            }

            // Botão para trocar moedas
            IconButton(
                onClick = onSwapCurrencies,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Trocar moedas",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Moeda de destino
            var expandedTo by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedTo,
                onExpandedChange = { expandedTo = it }
            ) {
                OutlinedTextField(
                    value = state.toCurrency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Para") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTo)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedTo,
                    onDismissRequest = { expandedTo = false }
                ) {
                    state.currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = {
                                Text("${currency.code} - ${currency.name}")
                            },
                            onClick = {
                                onToCurrencyChange(currency.code)
                                expandedTo = false
                            }
                        )
                    }
                }
            }

            // Botão de conversão
            Button(
                onClick = onConvert,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.canConvert && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Converter",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Info sobre as moedas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${state.fromCurrencyDisplay} → ${state.toCurrencyDisplay}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${state.currencies.size} moedas disponíveis",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}