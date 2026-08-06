package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = Blue200,
        onPrimary = Navy900,
        primaryContainer = Navy800,
        onPrimaryContainer = Blue100,
        secondary = AmberGold,
        onSecondary = Navy900,
        background = Navy900,
        onBackground = WhiteSurface,
        surface = Slate800,
        onSurface = WhiteSurface,
        surfaceVariant = Navy800,
        onSurfaceVariant = Slate300
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Navy800,
        onPrimary = WhiteSurface,
        primaryContainer = Blue100,
        onPrimaryContainer = Navy900,
        secondary = AmberGold,
        onSecondary = WhiteSurface,
        tertiary = EmeraldGreen,
        background = Blue50,
        onBackground = Slate800,
        surface = WhiteSurface,
        onSurface = Slate800,
        surfaceVariant = Slate100,
        onSurfaceVariant = Slate600
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set default dynamicColor to false to maintain the Professional Polish aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
