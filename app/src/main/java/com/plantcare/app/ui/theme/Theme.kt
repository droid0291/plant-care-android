package com.plantcare.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = Teal40,
    secondaryContainer = Teal80,
    background = androidx.compose.ui.graphics.Color(0xFFF6FBF4),
    surface = androidx.compose.ui.graphics.Color.White,
    error = Red40
)

private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green40,
    onPrimaryContainer = Green90,
    secondary = Teal80,
    background = androidx.compose.ui.graphics.Color(0xFF0D1B11),
    surface = androidx.compose.ui.graphics.Color(0xFF1A2D1F),
    error = Red80
)

@Composable
fun PlantCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
