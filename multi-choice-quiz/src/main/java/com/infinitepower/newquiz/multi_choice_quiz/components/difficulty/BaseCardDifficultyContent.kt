package com.infinitepower.newquiz.multi_choice_quiz.components.difficulty

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SignalCellularAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.spacing

@Composable
internal fun BaseCardDifficultyContent(
    modifier: Modifier = Modifier,
    text: String
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Column(modifier = modifier.padding(spaceMedium)) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(spaceMedium))
        Box(modifier = Modifier.padding(start = MaterialTheme.spacing.extraLarge)) {
            Icon(
                imageVector = Icons.Rounded.SignalCellularAlt,
                contentDescription = text,
                modifier = Modifier.size(35.dp)
            )
        }
    }
}