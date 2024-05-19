package com.infinitepower.newquiz.core.theme

import androidx.annotation.Keep
import androidx.annotation.Size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors

@Keep
@Immutable
data class CustomColor(
    val key: Key,
    val originalColor: Color,
    val harmonize: Boolean = true,
    val roles: ColorRoles = unspecifiedColorRoles
) {
    enum class Key {
        Blue,
        Green,
        Yellow,
        Red
    }

    @Keep
    @Immutable
    data class ColorRoles(
        val color: Color,
        val onColor: Color,
        val colorContainer: Color,
        val onColorContainer: Color
    )
}

private val unspecifiedColorRoles = CustomColor.ColorRoles(
    color = Color.Unspecified,
    onColor = Color.Unspecified,
    colorContainer = Color.Unspecified,
    onColorContainer = Color.Unspecified,
)

private fun ColorRoles.toColorRoles(): CustomColor.ColorRoles = CustomColor.ColorRoles(
    color = Color(this.accent),
    onColor = Color(this.onAccent),
    colorContainer = Color(this.accentContainer),
    onColorContainer = Color(this.onAccentContainer),
)

@Keep
@Immutable
data class ExtendedColors(
    val colors: List<CustomColor>
) {
    fun getColorsByKey(key: CustomColor.Key): CustomColor.ColorRoles {
        return colors.find { color -> color.key == key }?.roles
            ?: error("No color found for key $key")
    }

    fun getColorByKey(key: CustomColor.Key): Color = getColorsByKey(key).color
    fun getOnColorByKey(key: CustomColor.Key): Color = getColorsByKey(key).onColor
    fun getColorContainerByKey(key: CustomColor.Key): Color = getColorsByKey(key).colorContainer
    fun getOnColorContainerByKey(key: CustomColor.Key): Color = getColorsByKey(key).onColorContainer
}

private val initializeExtend = ExtendedColors(
    listOf(
        CustomColor(
            key = CustomColor.Key.Green,
            originalColor = Color(red = .3f, green = .6f, blue = .3f),
        ),
        CustomColor(
            key = CustomColor.Key.Yellow,
            originalColor = Color.Yellow,
        ),
        CustomColor(
            key = CustomColor.Key.Red,
            originalColor = Color.Red,
        ),
        CustomColor(
            key = CustomColor.Key.Blue,
            originalColor = Color.Blue,
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
        val shouldHarmonize = customColor.harmonize

        // Harmonize the color if needed, if not, use the original color to get the roles
        val color = if (shouldHarmonize) {
            MaterialColors.harmonize(
                customColor.originalColor.toArgb(),
                colorScheme.primary.toArgb()
            )
        } else {
            customColor.originalColor.toArgb()
        }

        val roles = MaterialColors.getColorRoles(color, isLight)
        customColor.copy(roles = roles.toColorRoles())
    }

    return ExtendedColors(colors)
}