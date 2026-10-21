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

// Standard Light Color Scheme (Material Design 3)
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,                 // Primary Forest Green
    onPrimary = Color.White,                // Text on Primary
    primaryContainer = GreenContainer,      // Light Emerald Container
    onPrimaryContainer = GreenPrimary,      // Text on Light Emerald Container

    secondary = GreenMain,                  // Secondary Accent Green
    onSecondary = Color.White,
    secondaryContainer = GreenSoft,
    onSecondaryContainer = GreenPrimary,

    tertiary = GreenMain,
    onTertiary = Color.White,
    tertiaryContainer = GreenContainer,
    onTertiaryContainer = GreenPrimary,

    background = Background,                // Standard Neutral Background
    onBackground = TextPrimary,             // Primary Text

    surface = Surface,                      // White Card Surface
    onSurface = TextPrimary,                // Text on Cards
    surfaceVariant = Color(0xFFF8FAFC),
    onSurfaceVariant = TextSecondary,       // Secondary Text

    error = ExpenseRed,
    onError = Color.White,
    errorContainer = ExpenseRedLight,
    onErrorContainer = ExpenseRed,

    outline = CardBorder,
    outlineVariant = DividerColor
)

// Standard Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = GreenLight,
    onPrimary = Color.Black,
    primaryContainer = GreenPrimary,
    onPrimaryContainer = Color.White,

    secondary = GreenSoft,
    onSecondary = Color.Black,

    background = Color(0xFF121212),
    onBackground = Color.White,

    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,

    error = ExpenseRed,
    onError = Color.White
)

@Composable
fun TambalaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,          // We use our clean brand color palette
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
