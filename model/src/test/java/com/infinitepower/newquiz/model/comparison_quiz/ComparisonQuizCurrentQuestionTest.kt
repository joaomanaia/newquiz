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
            value = 10
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 10
        )

        assertThrows<IllegalArgumentException> {
            ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)
        }
    }

    @Test
    fun `isCorrectAnswer returns true if mode is GREATER and first value is greater`() {
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 10
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 5
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        assertThat(question.isCorrectAnswer(ComparisonModeByFirst.GREATER)).isTrue()
    }

    @Test
    fun `isCorrectAnswer returns false if mode is GREATER and first value is less`() {
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 5
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 10
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        assertThat(question.isCorrectAnswer(ComparisonModeByFirst.GREATER)).isFalse()
    }

    @Test
    fun `isCorrectAnswer returns true if mode is LESS and first value is less`() {
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 5
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 10
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        assertThat(question.isCorrectAnswer(ComparisonModeByFirst.LESS)).isTrue()
    }

    @Test
    fun `isCorrectAnswer returns false if mode is LESS and first value is greater`() {
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 10
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 5
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        assertThat(question.isCorrectAnswer(ComparisonModeByFirst.LESS)).isFalse()
    }

    @Test
    fun `nextQuestion returns a new ComparisonQuizCurrentQuestion with the second question replaced`() {
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = uriMock,
            value = 5
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = uriMock,
            value = 10
        )

        val question = ComparisonQuizCurrentQuestion(quizItem1 to quizItem2)

        val newQuestion = ComparisonQuizItem(
            title = "C",
            imgUri = uriMock,
            value = 7
        )

        assertThat(question.questions.first).isEqualTo(quizItem1)
        assertThat(question.questions.second).isEqualTo(quizItem2)

        val updatedQuestion = question.nextQuestion(newQuestion)

        assertThat(updatedQuestion.questions.first).isEqualTo(quizItem2)
        assertThat(updatedQuestion.questions.second).isEqualTo(newQuestion)
    }
}