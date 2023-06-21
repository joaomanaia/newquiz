package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun QuizStepViewRow(
    modifier: Modifier = Modifier,
    questionSteps: List<MultiChoiceQuestionStep>,
    isResultsScreen: Boolean = false,
    onClick: (index: Int, questionStep: MultiChoiceQuestionStep) -> Unit = { _, _ -> }
) {
    val rowDescription = stringResource(id = CoreR.string.quiz_steps_row_container)

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = rowDescription
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
    ) {
        itemsIndexed(
            items = questionSteps,
            key = { _, step -> step.question.id }
        ) { index, step ->
            val position = index + 1

            QuizStepView(
                questionStep = step,
                position = position,
                enabled = isResultsScreen,
                onClick = { onClick(index, step) }
            )
        }
    }
}

@Composable
internal fun QuizStepView(
    modifier: Modifier = Modifier,
    questionStep: MultiChoiceQuestionStep,
    position: Int,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val stepBackgroundColor by animateColorAsState(
        targetValue =  if (questionStep is MultiChoiceQuestionStep.Current) {
            MaterialTheme.colorScheme.tertiary
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "Step background color"
    )

    val stepTextColor by animateColorAsState(
        targetValue = if (questionStep is MultiChoiceQuestionStep.Current) {
            MaterialTheme.colorScheme.onTertiary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "Step text color"
    )

    QuizStepView(
        modifier = modifier,
        questionStep = questionStep,
        position = position,
        stepBackgroundColor = stepBackgroundColor,
        stepTextColor = stepTextColor,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
internal fun QuizStepView(
    modifier: Modifier = Modifier,
    questionStep: MultiChoiceQuestionStep,
    position: Int,
    stepBackgroundColor: Color,
    stepTextColor: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        tonalElevation = 8.dp,
        modifier = modifier.size(35.dp),
        color = stepBackgroundColor,
        onClick = onClick,
        enabled = enabled
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = questionStep is MultiChoiceQuestionStep.Completed,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                val correct = questionStep is MultiChoiceQuestionStep.Completed && questionStep.correct

                Icon(
                    imageVector = if (correct) Icons.Rounded.Check else Icons.Rounded.Close,
                    contentDescription = if (correct) {
                        stringResource(id = CoreR.string.question_n_correct, position)
                    } else {
                        stringResource(id = CoreR.string.question_n_incorrect, position)
                    },
                    modifier = Modifier.size(25.dp),
                    tint = stepTextColor
                )
            }

            val textStyle = if (position >= 100) {
                MaterialTheme.typography.bodyMedium
            } else MaterialTheme.typography.titleLarge

            AnimatedVisibility(
                visible = questionStep !is MultiChoiceQuestionStep.Completed,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Text(
                    text = position.toString(),
                    color = stepTextColor,
                    style = textStyle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun AllStepsPreview() {
    val items = listOf(
        MultiChoiceQuestionStep.Completed(
            question = getBasicMultiChoiceQuestion(),
            correct = true
        ),
        MultiChoiceQuestionStep.Completed(
            question = getBasicMultiChoiceQuestion(),
            correct = false
        ),
        MultiChoiceQuestionStep.Current(question = getBasicMultiChoiceQuestion()),
        MultiChoiceQuestionStep.NotCurrent(question = getBasicMultiChoiceQuestion()),
    )

    NewQuizTheme(dynamicColor = false) {
        Surface {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                itemsIndexed(
                    items = items,
                    key = { index, _ -> index }
                ) { _, step ->
                    QuizStepView(
                        questionStep = step,
                        position = (1..9).random(),
                        onClick = {}
                    )
                }
            }
        }
    }
}