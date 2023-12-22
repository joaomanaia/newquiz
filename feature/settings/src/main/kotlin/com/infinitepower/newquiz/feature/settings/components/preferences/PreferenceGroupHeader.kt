package com.infinitepower.newquiz.feature.settings.components.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing

@Composable
internal fun LazyItemScope.PreferenceGroupHeader(title: String) {
    val spaceSmall = MaterialTheme.spacing.small
    val spaceMedium = MaterialTheme.spacing.medium

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillParentMaxWidth()
            .padding(bottom = spaceSmall, top = spaceMedium)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = spaceMedium),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
@PreviewLightDark
private fun PreferenceGroupHeaderPreview() {
    NewQuizTheme {
        Surface {
            LazyColumn {
                item {
                    PreferenceGroupHeader("NewSocial")
                }
            }
        }
    }
}
