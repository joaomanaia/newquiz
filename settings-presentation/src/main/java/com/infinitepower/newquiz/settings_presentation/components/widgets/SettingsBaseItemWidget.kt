package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
@ExperimentalMaterial3Api
internal fun NavigationButtonWidget(
    modifier: Modifier = Modifier,
    preference: Preference.PreferenceItem.NavigationButton
) {
    val screenExpanded = preference.screenExpanded
    val itemSelected = preference.itemSelected
    val inMainPage = preference.inMainPage

    val spaceMedium = MaterialTheme.spacing.medium
    val spaceSmall = MaterialTheme.spacing.small

    val rowPadding = if (screenExpanded && itemSelected) {
        PaddingValues(spaceMedium)
    } else {
        PaddingValues(horizontal = spaceMedium, vertical = spaceSmall)
    }

    val backgroundColor by animateColorAsState(
        targetValue = when {
            itemSelected -> MaterialTheme.colorScheme.primary
            screenExpanded && !inMainPage -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.surface
        }
    )

    val backgroundIconColor by animateColorAsState(
        targetValue = when {
            itemSelected -> MaterialTheme.colorScheme.onPrimary
            screenExpanded && !inMainPage -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.secondary
        }
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            itemSelected -> MaterialTheme.colorScheme.primary
            screenExpanded && !inMainPage -> MaterialTheme.colorScheme.onSecondary
            else -> MaterialTheme.colorScheme.onSecondary
        }
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = preference.onClick,
        shape = MaterialTheme.shapes.extraLarge,
        color = backgroundColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(rowPadding)
        ) {
            Surface(
                color = backgroundIconColor,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = preference.iconImageVector,
                    contentDescription = preference.title,
                    tint = iconColor,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Text(
                text = preference.title,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}