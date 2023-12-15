package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme

@Composable
fun PlayRandomQuizCard(
    modifier: Modifier = Modifier,
    title: String,
    buttonTitle: String,
    containerMainColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val backgroundColor = Brush.horizontalGradient(
        colors = listOf(
            containerMainColor,
            containerMainColor.copy(alpha = 0.8f)
        )
    )

    val textColor = if (enabled) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    }

    val cardShape = MaterialTheme.shapes.large

    Box(
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = onClick,
            role = Role.Button
        ).background(
            brush = backgroundColor,
            shape = cardShape,
            alpha = if (enabled) 1f else 0.12f
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = title,
                color = textColor,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                enabled = enabled,
            ) {
                Text(text = buttonTitle)
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun MultiChoiceCategoriesPreview() {
    NewQuizTheme {
        Surface {
            PlayRandomQuizCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                title = "PLay a quiz with random categories",
                buttonTitle = "Random Quiz",
                onClick = {},
                enabled = false
            )
        }
    }
}