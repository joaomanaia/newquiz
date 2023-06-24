package com.infinitepower.newquiz.model.comparison_quiz

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.URI

internal class ComparisonQuizCurrentQuestionTest {
    private val emptyUri = URI("")

    @Test
    fun `questions must have different values`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = emptyUri,
            value = 10.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = emptyUri,
            value = 10.0
        )

        assertThrows<IllegalArgumentException> {
            ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)
        }
    }

    @Test
    fun `nextQuestion returns a new ComparisonQuizCurrentQuestion with the second question replaced`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = emptyUri,
            value = 5.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = emptyUri,
            value = 10.0
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        val newQuestion = ComparisonQuizItem(
            title = "C",
            imgUri = emptyUri,
            value = 7.0
        )

        assertThat(question.questions.first).isEqualTo(quizItem1)
        assertThat(question.questions.second).isEqualTo(quizItem2)

        val updatedQuestion = question.nextQuestion(newQuestion)

        assertThat(updatedQuestion.questions.first).isEqualTo(quizItem2)
        assertThat(updatedQuestion.questions.second).isEqualTo(newQuestion)
    }

    @Test
    fun `test isCorrectAnswer when correct answer is first and the user answer is first`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = emptyUri,
            value = 2.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = emptyUri,
            value = 1.0
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        val isCorrectGreater = question.isCorrectAnswer(quizItem1, ComparisonMode.GREATER)
        assertThat(isCorrectGreater).isTrue()

        val isCorrectLower = question.isCorrectAnswer(quizItem1, ComparisonMode.LESSER)
        assertThat(isCorrectLower).isFalse()
    }

    @Test
    fun `test isCorrectAnswer when correct answer is first and the user answer is second`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = emptyUri,
            value = 2.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = emptyUri,
            value = 1.0
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        val isCorrectGreater = question.isCorrectAnswer(quizItem2, ComparisonMode.GREATER)
        assertThat(isCorrectGreater).isFalse()

        val isCorrectLower = question.isCorrectAnswer(quizItem2, ComparisonMode.LESSER)
        assertThat(isCorrectLower).isTrue()
    }

    @Test
    fun `test isCorrectAnswer when correct answer is second and the user answer is first`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = emptyUri,
            value = 1.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = emptyUri,
            value = 2.0
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        val isCorrectGreater = question.isCorrectAnswer(quizItem1, ComparisonMode.GREATER)
        assertThat(isCorrectGreater).isFalse()

        val isCorrectLower = question.isCorrectAnswer(quizItem1, ComparisonMode.LESSER)
        assertThat(isCorrectLower).isTrue()
    }

    @Test
    fun `test isCorrectAnswer when correct answer is second and the user answer is second`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = emptyUri,
            value = 1.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = emptyUri,
            value = 2.0
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        val isCorrectGreater = question.isCorrectAnswer(quizItem2, ComparisonMode.GREATER)
        assertThat(isCorrectGreater).isTrue()

        val isCorrectLower = question.isCorrectAnswer(quizItem2, ComparisonMode.LESSER)
        assertThat(isCorrectLower).isFalse()
    }
}