package com.infinitepower.newquiz.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.text.CompactDecimalText
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun UserXpAndLevelCard(
    modifier: Modifier = Modifier,
    totalXp: ULong,
    level: UInt
) {
    Surface(
        modifier = modifier.height(90.dp),
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {
            CardItem(
                modifier = Modifier.weight(1f),
                title = stringResource(id = CoreR.string.level),
                value = level.toInt()
            )
            VerticalDivider()
            CardItem(
                modifier = Modifier.weight(1f),
                title = stringResource(id = CoreR.string.total_xp),
                value = totalXp.toInt()
            )
        }
    }
}

@Composable
private fun CardItem(
    modifier: Modifier = Modifier,
    title: String,
    value: Int
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.CenterVertically)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        CompactDecimalText(
            value = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
@PreviewLightDark
private fun UserXpAndLevelCardPreview() {
    NewQuizTheme {
        Surface {
            UserXpAndLevelCard(
                modifier = Modifier.padding(16.dp),
                totalXp = 1234u,
                level = 1u
            )
        }
    }
}
