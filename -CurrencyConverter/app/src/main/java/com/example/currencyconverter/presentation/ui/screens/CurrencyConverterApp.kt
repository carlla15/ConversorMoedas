package com.example.currencyconverter.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.di.ViewModelFactory
import com.example.currencyconverter.presentation.viewmodel.CurrencyViewModel

@Composable
fun CurrencyConverterApp(navController: NavHostController = rememberNavController()) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: CurrencyViewModel = viewModel(factory = ViewModelFactory())

    NavHost(
        navController = navController,
        startDestination = Screens.Converter.route
    ) {
        composable(Screens.Converter.route) {
            ConverterScreen(
                navController = navController,
                viewModel = viewModel,
                coroutineScope = coroutineScope
            )
        }
        composable(Screens.History.route) {
            HistoryScreen(
                navController = navController,
                viewModel = viewModel,
                coroutineScope = coroutineScope
            )
        }
    }
}

sealed class Screens(val route: String) {
    object Converter : Screens("converter")
    object History : Screens("history")
}