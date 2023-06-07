package com.infinitepower.newquiz.comparison_quiz.list.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val iconContainerColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val iconColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)

    val spaceMedium = MaterialTheme.spacing.medium

    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
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
