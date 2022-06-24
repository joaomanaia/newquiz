package com.infinitepower.newquiz.compose.core.util.quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.compose.model.question.QuestionStep
import com.infinitepower.newquiz.compose.data.local.question.getBasicQuestion
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class QuizXPUtilTest {
    private lateinit var quizXPUtil: QuizXPUtil

    @BeforeEach
    fun setup() {
        quizXPUtil = QuizXPUtil()
    }

    @Test
    fun `getNewUserXPByQuizSteps text correct xp`() {
        val quizStep = listOf(
            com.infinitepower.newquiz.compose.model.question.QuestionStep.Completed(getBasicQuestion(), correct = true),
            com.infinitepower.newquiz.compose.model.question.QuestionStep.Completed(getBasicQuestion(), correct = true),
            com.infinitepower.newquiz.compose.model.question.QuestionStep.Completed(getBasicQuestion(), correct = false),
            com.infinitepower.newquiz.compose.model.question.QuestionStep.Completed(getBasicQuestion(), correct = false),
            com.infinitepower.newquiz.compose.model.question.QuestionStep.Completed(getBasicQuestion(), correct = true),
        )

        val xp = quizXPUtil.getNewUserXPByQuizSteps(quizStep)
        assertThat(xp).isEqualTo(90)
    }
}