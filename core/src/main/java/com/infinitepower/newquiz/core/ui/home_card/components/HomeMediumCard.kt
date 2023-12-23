package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem

@Composable
@ExperimentalMaterial3Api
fun HomeMediumCard(
    modifier: Modifier = Modifier,
    data: HomeCardItem.MediumCard
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val title = stringResource(id = data.title)

    val isInspectionMode = LocalInspectionMode.current

    Card(
        onClick = data.onClick,
        enabled = data.enabled || isInspectionMode,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(spaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeCardIcon(
                icon = data.icon,
                contentDescription = title,
                modifier = Modifier
                    .size(75.dp)
                    .padding(MaterialTheme.spacing.small),
            )
            Spacer(modifier = Modifier.width(spaceMedium))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                if (data.description != null) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeMediumCardPreview() {
    NewQuizTheme {
        Surface {
            HomeMediumCard(
                data = HomeCardItem.MediumCard(
                    title = R.string.quick_quiz,
                    description = "10 Questions",
                    icon = CardIcon.Icon(Icons.Rounded.Check),
                    onClick = {},
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}