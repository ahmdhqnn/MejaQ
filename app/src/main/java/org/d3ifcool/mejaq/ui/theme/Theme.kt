package org.d3ifcool.mejaq.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Warna Desain
val MejaqRed = Color(0xFFD91E0A)
val MejaqPink = Color(0xFFFDE8E5)
val MejaqLightGray = Color(0xFFF7F7F7)
val MejaqDarkGray = Color(0xFF4A4A4A)
val MejaqWhite = Color(0xFFFFFFFF)
val MejaqBlack = Color(0xFF000000)

private val LightColorScheme = lightColorScheme(
    primary = MejaqRed,
    onPrimary = MejaqWhite,
    secondary = MejaqPink,
    onSecondary = MejaqRed,
    background = MejaqLightGray,
    onBackground = MejaqDarkGray,
    surface = MejaqWhite,
    onSurface = MejaqBlack,
)

@Composable
fun MejaqAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}