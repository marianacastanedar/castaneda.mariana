package com.example.castanedamariana.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue400,
    onPrimary = Earth900,
    primaryContainer = SkyBlue700,
    onPrimaryContainer = Sand100,
    
    secondary = Sand400,
    onSecondary = Earth900,
    secondaryContainer = Sand700,
    onSecondaryContainer = Sand100,
    
    tertiary = Terracotta,
    onTertiary = Earth50,
    tertiaryContainer = TerracottaDark,
    onTertiaryContainer = Sand100,
    
    background = Earth900,
    onBackground = Sand100,
    
    surface = Earth800,
    onSurface = Sand100,
    surfaceVariant = Earth700,
    onSurfaceVariant = Sand200,
    
    error = NegativeRed,
    onError = Earth50,
    errorContainer = NegativeRedLight,
    onErrorContainer = Earth900,
    
    outline = Sand600,
    outlineVariant = Sand800
)

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue600,
    onPrimary = Sand50,
    primaryContainer = SkyBlue200,
    onPrimaryContainer = SkyBlue900,
    
    secondary = Sand600,
    onSecondary = Sand50,
    secondaryContainer = Sand200,
    onSecondaryContainer = Sand900,
    
    tertiary = Terracotta,
    onTertiary = Sand50,
    tertiaryContainer = TerracottaLight,
    onTertiaryContainer = Earth900,
    
    background = Sand50,
    onBackground = Earth900,
    
    surface = Sand100,
    onSurface = Earth900,
    surfaceVariant = Sand200,
    onSurfaceVariant = Earth800,
    
    error = NegativeRed,
    onError = Sand50,
    errorContainer = NegativeRedLight,
    onErrorContainer = Earth900,
    
    outline = Sand500,
    outlineVariant = Sand300
)

@Composable
fun CryptoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
