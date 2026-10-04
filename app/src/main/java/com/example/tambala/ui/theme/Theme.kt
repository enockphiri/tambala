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

// Light Color Scheme (Main one we will use)
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,                 // Dark green - buttons, selected items
    onPrimary = Color.White,                // Text/icons on primary buttons
    primaryContainer = GreenContainer,      // Light green containers
    onPrimaryContainer = GreenPrimary,      // Text on light green containers

    secondary = GreenMain,                  // Secondary green
    onSecondary = Color.White,
    secondaryContainer = GreenSoft,
    onSecondaryContainer = GreenPrimary,

    tertiary = PurpleIcon,
    onTertiary = Color.White,
    tertiaryContainer = PurpleSoft,
    onTertiaryContainer = PurpleIcon,

    background = Background,                // App background
    onBackground = TextPrimary,             // Main text on background

    surface = Surface,                      // Cards and surfaces
    onSurface = TextPrimary,                // Text on cards
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,      // Secondary text

    error = ExpenseRed,
    onError = Color.White,
    errorContainer = ExpenseRedLight,
    onErrorContainer = ExpenseRed,

    outline = DividerColor,
    outlineVariant = DividerColor
)

// Dark Color Scheme (Optional - basic version)
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
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,          // We disable dynamic color to keep our green brand
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            // We force our custom colors instead of dynamic
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
        typography = Typography,          // Uses the default Type.kt
        content = content
    )
}