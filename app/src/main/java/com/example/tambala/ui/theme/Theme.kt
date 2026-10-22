package com.example.tambala.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Modern Neobank Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueContainer,
    onPrimaryContainer = PrimaryBlue,

    secondary = PrimaryBlueLight,
    onSecondary = Color.White,
    secondaryContainer = PrimaryBlueContainer,
    onSecondaryContainer = PrimaryBlue,

    tertiary = PrimaryBlueLight,
    onTertiary = Color.White,
    tertiaryContainer = PrimaryBlueContainer,
    onTertiaryContainer = PrimaryBlue,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,

    error = ExpenseRose,
    onError = Color.White,
    errorContainer = ExpenseRoseLight,
    onErrorContainer = ExpenseRose,

    outline = CardBorder,
    outlineVariant = DividerColor
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = Color.Black,
    primaryContainer = PrimaryBlue,
    onPrimaryContainer = Color.White,

    secondary = PrimaryBlueContainer,
    onSecondary = Color.Black,

    background = Color(0xFF0F172A),
    onBackground = Color.White,

    surface = Color(0xFF1E293B),
    onSurface = Color.White,

    error = ExpenseRose,
    onError = Color.White
)

@Composable
fun TambalaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
