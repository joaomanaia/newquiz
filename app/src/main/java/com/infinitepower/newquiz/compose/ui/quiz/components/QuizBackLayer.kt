package com.infinitepower.newquiz.compose.ui.quiz.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infinitepower.newquiz.compose.model.quiz.QuizStep
import com.infinitepower.newquiz.compose.model.quiz.getBasicQuestion
import com.infinitepower.newquiz.compose.ui.quiz.toMinuteSecond

@Composable
fun QuizBackLayer(
    quizSteps: List<QuizStep>,
    remainingTime: Int = 0
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(
                items = quizSteps,
                key = {
                    it.question.id
                }
            ) { quizStep ->
                QuizStepView(quizStep = quizStep, position = quizSteps.indexOf(quizStep) + 1)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Remaining Time: ${remainingTime.toMinuteSecond()}s",
            fontSize = 17.sp
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = -10354450)
fun QuizBackLayerPreview() {
    QuizBackLayer(
        quizSteps = listOf(
            QuizStep(question = getBasicQuestion(), current = false, completed = true, correct = false),
            QuizStep(question = getBasicQuestion(), current = false, completed = true, correct = true),
            QuizStep(question = getBasicQuestion(), current = true, completed = false, correct = false),
            QuizStep(question = getBasicQuestion(), current = false, completed = false, correct = false)
        ),
        remainingTime = 23
    )
}