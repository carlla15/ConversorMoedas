package com.example.currencyconverter.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// CORES EXTRA (se precisar usar em componentes específicos)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// CORES VERDES PERSONALIZADAS (adicione estas)
val LightGreen = Color(0xFF8BC34A)
val MediumGreen = Color(0xFF4CAF50)
val DarkGreen = Color(0xFF2E7D32)
val ExtraLightGreen = Color(0xFFE8F5E9)
val BackgroundWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF333333)
val BorderGray = Color(0xFFE0E0E0)

// TEMA CLARO COM VERDES - FUNDO BRANCO 🎨
private val LightColorScheme = lightColorScheme(
    // Cores principais (verdes financeiros)
    primary = DarkGreen,                    // Verde escuro - botões principais
    onPrimary = Color.White,                // Texto branco sobre verde

    secondary = MediumGreen,                // Verde médio - botões secundários
    onSecondary = Color.White,

    tertiary = LightGreen,                  // Verde claro - destaques
    onTertiary = Color.Black,

    // FUNDOS SEMPRE CLAROS ⚪
    background = BackgroundWhite,           // Fundo TOTALMENTE BRANCO
    onBackground = TextDark,                // Texto cinza escuro sobre branco

    // Superfícies (cards, containers)
    surface = Color(0xFFF8F9FA),            // Cinza MUITO claro para cards
    onSurface = DarkGreen,                  // Texto verde sobre cards

    // Containers especiais
    primaryContainer = ExtraLightGreen,     // Verde super claro
    onPrimaryContainer = Color(0xFF1B5E20),

    // Estados
    error = Color(0xFFD32F2F),              // Vermelho para erros
    onError = Color.White,

    // Bordas e divisores
    outline = BorderGray,                   // Cinza claro para bordas
    surfaceVariant = Color(0xFFF1F8E9)      // Verde muito muito claro
)

@Composable
fun CurrencyConverterTheme(
    content: @Composable () -> Unit
) {
    // SEMPRE usa o tema claro ⬇️
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            // Status bar sempre clara (texto escuro)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}