package com.infinitepower.newquiz.core.ui.components.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing

/**
 * A badge that indicates that the category requires an internet connection or not.
 * When clicked, it shows a text that explains its meaning.
 *
 * @param modifier The modifier to be applied to the component.
 * @param requireConnection Whether the category requires an internet connection or not.
 * @param showTextByDefault Whether the text should be shown by default.
 * @param color The color of the badge.
 * @param shape The shape of the badge.
 */
@Composable
internal fun CategoryConnectionInfoBadge(
    modifier: Modifier = Modifier,
    requireConnection: Boolean = true,
    showTextByDefault: Boolean = false,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    shape: Shape = MaterialTheme.shapes.medium,
) {
    val (showText, setShowText) = remember {
        mutableStateOf(showTextByDefault)
    }

    val description = stringResource(
        id = if (requireConnection) {
            R.string.require_internet_connection
        } else {
            R.string.dont_require_internet_connection
        }
    )

    Surface(
        modifier = modifier,
        color = color,
        shape = shape,
        onClick = { setShowText(!showText) }
    ) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.spacing.default)
                .semantics(
                    mergeDescendants = true,
                ) {
                    contentDescription = description
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default)
        ) {
            Icon(
                imageVector = if (requireConnection) {
                    Icons.Rounded.Wifi
                } else {
                    Icons.Rounded.WifiOff
                },
                contentDescription = null
            )
            AnimatedVisibility(visible = showText) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun CategoryConnectionInfoBadgePreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) requireConnection: Boolean
) {
    NewQuizTheme {
        Surface {
            CategoryConnectionInfoBadge(
                modifier = Modifier.padding(16.dp),
                requireConnection = requireConnection
            )
        }
    }
}
