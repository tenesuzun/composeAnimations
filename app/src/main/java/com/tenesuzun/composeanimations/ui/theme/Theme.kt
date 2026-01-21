package com.tenesuzun.composeanimations.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tenesuzun.composeanimations.ui.effects.EffectMode

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ModernTheme(content: @Composable () -> Unit) {
    val scheme = darkColorScheme(
        // Keep base modern/dark
        primary = Color(0xFF8B5CF6),   // violet
        secondary = Color(0xFF22D3EE), // cyan
        tertiary = Color(0xFFFFB86B),  // warm accent
        background = Color(0xFF070A12),
        surface = Color(0xFF0E1424),
        surfaceVariant = Color(0xFF151F36),
        onBackground = Color(0xFFE7EAF0),
        onSurface = Color(0xFFE7EAF0),
        onSurfaceVariant = Color(0xFFB8C2D6),
    )
    MaterialTheme(colorScheme = scheme, content = content)
}

fun appBackgroundBrush(): Brush =
    Brush.linearGradient(
        colors = listOf(
            Color(0xFF2D0475),
            Color(0xFF8D255A),
            Color(0xFF2D0909),
        )
    )

fun effectAccent(mode: EffectMode): Color = when (mode) {
    EffectMode.HoverGlow -> Color(0xFF22D3EE)      // cyan
    EffectMode.DynamicShadow -> Color(0xFF8B5CF6)  // violet
    EffectMode.BlurOnScroll -> Color(0xFFFFB86B)   // warm
    EffectMode.Glassmorphism -> Color(0xFF4ADE80)  // green-ish for contrast
    EffectMode.Neumorphism -> Color(0xFF94A3B8)    // slate
}


// Boş proje açılınca varsayılan olan
@Composable
fun ComposeAnimationsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}