package com.infinitepower.newquiz.compose.ui.quiz.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
internal fun CardQuestion(
    description: String,
    selected: Boolean,
    isResults: Boolean = false,
    resultAnswerCorrect: Boolean = false,
    onClick: () -> Unit
) {
    val color = animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.primary
            isResults && resultAnswerCorrect -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.surface
        }
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CircleShape,
        tonalElevation = 8.dp,
        role = Role.Checkbox,
        onClick = onClick,
        color = color.value,
        indication = rememberRipple()
    ) {
        Text(
            text = description,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}