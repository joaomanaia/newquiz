package com.infinitepower.newquiz.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import com.google.android.material.color.MaterialColors

private val LightThemeColors = lightColorScheme()

private val DarkThemeColors = darkColorScheme()

private fun setupErrorColors(
    colorScheme: ColorScheme,
    isLight: Boolean
): ColorScheme {
    val harmonizedError = MaterialColors.harmonize(colorScheme.error.toArgb(), colorScheme.primary.toArgb())
    val roles = MaterialColors.getColorRoles(harmonizedError, isLight)

    //returns a colorScheme with newly harmonized error colors
    return colorScheme.copy(
        error = Color(roles.accent),
        onError = Color(roles.onAccent),
        errorContainer = Color(roles.accentContainer),
        onErrorContainer = Color(roles.onAccentContainer)
    )
}

@Composable
fun NewQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    lightColorScheme: ColorScheme = LightThemeColors,
    darkColorScheme: ColorScheme = DarkThemeColors,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    val colorsWithHarmonizedError = setupErrorColors(colorScheme, !darkTheme)
    val extendedColors = setupCustomColors(colorScheme, !darkTheme)

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalExtendedColors provides extendedColors,
    ) {
        MaterialTheme(
            colorScheme = colorsWithHarmonizedError,
            content = content,
            typography = AppTypography
        )
    }
}