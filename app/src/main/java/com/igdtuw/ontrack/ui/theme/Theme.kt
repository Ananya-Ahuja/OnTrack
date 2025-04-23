package com.igdtuw.ontrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Unified Color Scheme
private val LightColors = lightColorScheme(
    primary = Color(0xFFA152E6),       // Primary purple
    onPrimary = Color.White,           // White text on purple
    secondary = Color(0xFF6C68F8),     // Secondary color
    surface = Color.White,             // Background for cards/sheets
    background = Color(0xFFF7F9FB),    // Page background
    onBackground = Color(0xFF2D0A37)   // Primary text color
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA152E6),
    onPrimary = Color.White,
    secondary = Color(0xFF6C68F8),
    surface = Color(0xFF181828),
    background = Color(0xFF1A1A1A),
    onBackground = Color.White
)

// Consolidated Typography
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = Color(0xFF2D0A37)
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.White
    )
)

// Onboarding Title and Subtitle Styles
val TitleStyle = TextStyle(
    fontSize = 28.sp,
    fontWeight = FontWeight.Bold,
    color = Color(0xFF2D0A37),
    textAlign = TextAlign.Center
)

val SubtitleStyle = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Normal,
    color = Color(0xFF4A4458),
    textAlign = TextAlign.Center
)

@Composable
fun UiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Force-disable dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Always use our custom color schemes
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}