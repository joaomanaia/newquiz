package com.infinitepower.newquiz.core.theme

import androidx.annotation.Keep
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors

@Keep
data class CustomColor(
    val key: Keys,
    val color: Color,
    val harmonized: Boolean,
    val roles: ColorRoles
) {
    enum class Keys {
        Blue,
        Green,
        Yellow
    }

    @Keep
    data class ColorRoles(
        val accent: Color,
        val onAccent: Color,
        val accentContainer: Color,
        val onAccentContainer: Color
    )
}

private fun initializeColorRoles() = CustomColor.ColorRoles(
    accent = Color.Unspecified,
    onAccent = Color.Unspecified,
    accentContainer = Color.Unspecified,
    onAccentContainer = Color.Unspecified,
)

private fun ColorRoles.toColorRoles(): CustomColor.ColorRoles = CustomColor.ColorRoles(
    accent = Color(this.accent),
    onAccent = Color(this.onAccent),
    accentContainer = Color(this.accentContainer),
    onAccentContainer = Color(this.onAccentContainer),
)

@Keep
data class ExtendedColors(
    val colors: List<CustomColor>
) {
    @Composable
    @ReadOnlyComposable
    fun getColorRolesByKey(
        key: CustomColor.Keys
    ): CustomColor.ColorRoles {
        val color = colors.find { color -> color.key == key }

        return if (color != null && color.harmonized) {
            color.roles
        } else {
            CustomColor.ColorRoles(
                accent = color?.color ?: MaterialTheme.colorScheme.primary,
                onAccent = color?.color ?: MaterialTheme.colorScheme.onPrimary,
                accentContainer = color?.color ?: MaterialTheme.colorScheme.primaryContainer,
                onAccentContainer = color?.color ?: MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }

    @Composable
    @ReadOnlyComposable
    fun getColorAccentByKey(
        key: CustomColor.Keys
    ): Color = getColorRolesByKey(key = key).accent

    @Composable
    @ReadOnlyComposable
    fun getColorOnAccentByKey(
        key: CustomColor.Keys
    ): Color = getColorRolesByKey(key = key).onAccent
}

private val initializeExtend = ExtendedColors(
    listOf(
        CustomColor(
            key = CustomColor.Keys.Green,
            color = Color(red = .3f, green = .6f, blue = .3f),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Yellow,
            color = Color.Yellow,
            harmonized = true,
            roles = initializeColorRoles()
        ),
    )
)

val LocalExtendedColors = staticCompositionLocalOf {
    initializeExtend
}

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current

internal fun setupCustomColors(
    colorScheme: ColorScheme,
    isLight: Boolean
): ExtendedColors {
    val colors = initializeExtend.colors.map { customColor ->
        // Retrieve record
        val shouldHarmonize = customColor.harmonized
        // Blend or not
        if (shouldHarmonize) {
            val blendedColor = MaterialColors.harmonize(customColor.color.toArgb(), colorScheme.primary.toArgb())

            val roles = MaterialColors.getColorRoles(blendedColor, isLight)

            customColor.copy(roles = roles.toColorRoles())
        } else {
            val roles = MaterialColors.getColorRoles(customColor.color.toArgb(), isLight)
            customColor.copy(roles = roles.toColorRoles())
        }
    }
    return ExtendedColors(colors)
}