package com.infinitepower.newquiz.feature.settings.components.preferences.widgets

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.feature.settings.model.Preference

@Composable
internal fun NavigationButtonWidget(
    modifier: Modifier = Modifier,
    preference: Preference.PreferenceItem.NavigationButton
) {
    NavigationButton(
        modifier = modifier,
        title = preference.title,
        icon = preference.icon,
        description = preference.summary,
        isSelected = preference.itemSelected,
        enabled = preference.enabled,
        singleLineTitle = preference.singleLineTitle,
        singleLineSummary = preference.singleLineSummary,
        onClick = preference.onClick,
    )
}

@Composable
internal fun NavigationButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: @Composable (() -> Unit)?,
    description: String? = null,
    isSelected: Boolean = false,
    enabled: Boolean = true,
    singleLineTitle: Boolean = true,
    singleLineSummary: Boolean = true,
    onClick: () -> Unit,
) {
    val transition = updateTransition(
        targetState = isSelected,
        label = "Navigation Button"
    )

    val containerColor = transition.animateColor(
        label = "Container Color"
    ) { selected ->
        if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        }
    }

    Surface(
        modifier = modifier,
        selected = isSelected,
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        color = containerColor.value,
        enabled = enabled,
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = title,
                    maxLines = if (singleLineTitle) 1 else Int.MAX_VALUE
                )
            },
            supportingContent = description?.let {
                {
                    Text(
                        text = it,
                        maxLines = if (singleLineSummary) 1 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            leadingContent = icon,
            colors = ListItemDefaults.colors(
                containerColor = containerColor.value
            ),
            modifier = Modifier.padding(MaterialTheme.spacing.small),
        )
    }
}

@Composable
@PreviewLightDark
private fun NavigationButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isSelected: Boolean,
) {
    NewQuizTheme {
        Surface {
            NavigationButton(
                title = "Settings",
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Build,
                        contentDescription = null
                    )
                },
                description = "Settings description",
                isSelected = isSelected,
                onClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
