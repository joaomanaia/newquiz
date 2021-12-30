package com.infinitepower.newquiz.compose.ui.quiz.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.compose.model.quiz.QuizStep
import com.infinitepower.newquiz.compose.model.quiz.getBasicQuestion
import com.infinitepower.newquiz.compose.ui.theme.NewQuizTheme

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun QuizStepView(
    quizStep: QuizStep,
    position: Int
) {
    val stepBackgroundColor by animateColorAsState(
        targetValue = if (quizStep.current) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    )

    val stepTextColor by animateColorAsState(
        targetValue = if (quizStep.current) MaterialTheme.colorScheme.inverseOnSurface else LocalTextStyle.current.color
    )

    Surface(
        shape = CircleShape,
        tonalElevation = 8.dp,
        modifier = Modifier.size(35.dp),
        color = stepBackgroundColor
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = quizStep.completed,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Icon(
                    imageVector = if (quizStep.correct) Icons.Rounded.Check else Icons.Rounded.Close,
                    contentDescription = "Question $position ${if (quizStep.correct) "correct" else "wrong"}",
                    Modifier.size(25.dp),
                )
            }

            AnimatedVisibility(
                visible = !quizStep.completed,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Text(
                    text = position.toString(),
                    color = stepTextColor,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    name = "All Steps Preview"
)
@Preview(
    showBackground = true,
    name = "All Steps Preview Night",
    uiMode = UI_MODE_NIGHT_YES
)
private fun AllStepsPreview() {
    val items = listOf(
        QuizStep(
            question = getBasicQuestion(),
            current = false,
            completed = true,
            correct = true
        ),
        QuizStep(
            question = getBasicQuestion(),
            current = false,
            completed = true,
            correct = false
        ),
        QuizStep(
            question = getBasicQuestion(),
            current = false,
            completed = false,
            correct = false
        ),
        QuizStep(
            question = getBasicQuestion(),
            current = true,
            completed = false,
            correct = false
        ),
    )

    NewQuizTheme {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(
                items = items,
                key = { index, step -> index }
            ) { index, step ->
                QuizStepView(quizStep = step, position = (1..9).random())
            }
        }
    }
}