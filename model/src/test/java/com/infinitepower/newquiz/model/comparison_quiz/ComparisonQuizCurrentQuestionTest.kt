package com.infinitepower.newquiz.model.comparison_quiz

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ComparisonQuizCurrentQuestionTest {
    @Test
    fun `questions must have different values`() {
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 10.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 10.0
        )

        assertThrows<IllegalArgumentException> {
            ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)
        }
    }

    @Test
    fun `nextQuestion returns a new ComparisonQuizCurrentQuestion with the second question replaced`() {
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 5.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 10.0
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        val newQuestion = ComparisonQuizItem(
            title = "C",
            imgUri = uriMock,
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
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 2.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
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
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 2.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
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
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 1.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
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
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 1.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 2.0
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        val isCorrectGreater = question.isCorrectAnswer(quizItem2, ComparisonMode.GREATER)
        assertThat(isCorrectGreater).isTrue()

        val isCorrectLower = question.isCorrectAnswer(quizItem2, ComparisonMode.LESSER)
        assertThat(isCorrectLower).isFalse()
    }
}