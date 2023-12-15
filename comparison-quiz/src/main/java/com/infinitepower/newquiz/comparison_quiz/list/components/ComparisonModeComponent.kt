package com.infinitepower.newquiz.comparison_quiz.list.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode

@Composable
internal fun ComparisonModeComponent(
    modifier: Modifier = Modifier,
    selected: Boolean,
    enabled: Boolean = true,
    mode: ComparisonMode,
    shape: Shape = ComparisonModeDefaults.shape,
    colors: ComparisonModeColors = ComparisonModeDefaults.defaultColors(),
    onClick: () -> Unit = {}
) {
    val title = when (mode) {
        ComparisonMode.GREATER -> stringResource(id = R.string.greater)
        ComparisonMode.LESSER -> stringResource(id = R.string.lesser)
    }

    val icon = when (mode) {
        ComparisonMode.GREATER -> Icons.Rounded.ChevronRight
        ComparisonMode.LESSER -> Icons.Rounded.ChevronLeft
    }

    val transition = updateTransition(targetState = selected, label = "$title Mode")

    val containerColor = colors.containerColor(transition).value
    val contentColor = colors.contentColor(transition).value
    val iconColor = colors.iconColor(transition).value
    val iconContainerColor = colors.iconContainerColor(transition).value

    val border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)

    val spaceMedium = MaterialTheme.spacing.medium

    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
        selected = selected,
        enabled = enabled
    ) {
        Column(
            modifier = Modifier.padding(spaceMedium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Surface(
                    shape = CircleShape,
                    color = iconContainerColor,
                    contentColor = iconColor
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(id = R.string.icon_of_s, title),
                        modifier = Modifier.padding(MaterialTheme.spacing.extraSmall)
                    )
                }
            }
        }
    }
}

object ComparisonModeDefaults {
    val shape: Shape @Composable get() = MaterialTheme.shapes.large

    @Composable
    fun defaultColors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        iconColor: Color = MaterialTheme.colorScheme.onSurface,
        selectedIconColor: Color = MaterialTheme.colorScheme.primary,
        iconContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        selectedIconContainerColor: Color = MaterialTheme.colorScheme.onPrimary
    ): ComparisonModeColors = ComparisonModeColors(
        containerColor = containerColor,
        selectedContainerColor = selectedContainerColor,
        contentColor = contentColor,
        selectedContentColor = selectedContentColor,
        iconColor = iconColor,
        selectedIconColor = selectedIconColor,
        iconContainerColor = iconContainerColor,
        selectedIconContainerColor = selectedIconContainerColor
    )
}

@Immutable
class ComparisonModeColors internal constructor(
    private val containerColor: Color,
    private val selectedContainerColor: Color,
    private val contentColor: Color,
    private val selectedContentColor: Color,
    private val iconColor: Color,
    private val selectedIconColor: Color,
    private val iconContainerColor: Color,
    private val selectedIconContainerColor: Color
) {
    @Composable
    internal fun containerColor(
        transition: Transition<Boolean>
    ): State<Color> {
        return transition.animateColor(
            label = "Container Color"
        ) { selected ->
            if (selected) selectedContainerColor else containerColor
        }
    }

    @Composable
    internal fun contentColor(
        transition: Transition<Boolean>
    ): State<Color> {
        return transition.animateColor(
            label = "Content Color"
        ) { selected ->
            if (selected) selectedContentColor else contentColor
        }
    }

    @Composable
    internal fun iconColor(
        transition: Transition<Boolean>
    ): State<Color> {
        return transition.animateColor(
            label = "Icon Color"
        ) { selected ->
            if (selected) selectedIconColor else iconColor
        }
    }

    @Composable
    internal fun iconContainerColor(
        transition: Transition<Boolean>
    ): State<Color> {
        return transition.animateColor(
            label = "Icon Container Color"
        ) { selected ->
            if (selected) selectedIconContainerColor else iconContainerColor
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ComparisonModeColors) return false

        if (containerColor != other.containerColor) return false
        if (selectedContainerColor != other.selectedContainerColor) return false
        if (contentColor != other.contentColor) return false
        if (selectedContentColor != other.selectedContentColor) return false
        if (iconColor != other.iconColor) return false
        if (selectedIconColor != other.selectedIconColor) return false
        if (iconContainerColor != other.iconContainerColor) return false
        if (selectedIconContainerColor != other.selectedIconContainerColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + selectedContainerColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + selectedContentColor.hashCode()
        result = 31 * result + iconColor.hashCode()
        result = 31 * result + selectedIconColor.hashCode()
        result = 31 * result + iconContainerColor.hashCode()
        result = 31 * result + selectedIconContainerColor.hashCode()
        return result
    }
}

@Composable
@PreviewNightLight
private fun ComparisonModeComponentPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) selected: Boolean
) {
    NewQuizTheme {
        Surface {
            ComparisonModeComponent(
                modifier = Modifier
                    .padding(16.dp)
                    .width(120.dp),
                mode = ComparisonMode.GREATER,
                onClick = {},
                selected = selected
            )
        }
    }
}
