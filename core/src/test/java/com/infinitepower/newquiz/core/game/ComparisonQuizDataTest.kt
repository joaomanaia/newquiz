package com.infinitepower.newquiz.core.game

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.testing.data.fake.FakeComparisonQuizData
import com.infinitepower.newquiz.core.testing.utils.mockAndroidLog
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizQuestion
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.URI
import kotlin.test.BeforeTest

class ComparisonQuizDataTest {
    @BeforeTest
    fun setUp() {
        mockAndroidLog()
    }

    @Test
    fun `nextQuestion should return a new ComparisonQuizData object with a new current question`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = URI(""),
            value = 10.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = URI(""),
            value = 5.0
        )

        val quizData = ComparisonQuizCore.QuizData(
            questions = listOf(quizItem1, quizItem2),
            currentQuestion = null,
            questionDescription = "Which country has more population?",
            comparisonMode = ComparisonMode.GREATER,
            category = FakeComparisonQuizData.generateCategory()
        )

        assertThat(quizData.questions).containsExactly(quizItem1, quizItem2)
        assertThat(quizData.currentQuestion).isNull()

        val nextQuizData = quizData.getNextQuestion()

        assertThat(nextQuizData.questions).containsNoneOf(quizItem1, quizItem2)
        assertThat(nextQuizData.questions).isEmpty()
        assertThat(nextQuizData.currentQuestion).isNotNull()
    }

    @Test
    fun `nextQuestion should return throw IllegalStateException when questions list is empty`() {
        val quizData = ComparisonQuizCore.QuizData(
            questions = emptyList(),
            currentQuestion = null,
            questionDescription = "Which country has more population?",
            comparisonMode = ComparisonMode.GREATER,
            category = FakeComparisonQuizData.generateCategory()
        )

        assertThat(quizData.questions).isEmpty()
        assertThat(quizData.currentQuestion).isNull()

        val exception = assertThrows<GameOverException> {
            quizData.getNextQuestion()
        }

        assertThat(exception).hasMessageThat().isEqualTo("Questions list is empty")
    }

    @Test
    fun `test nextQuestion when questions has size of 3`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = URI(""),
            value = 10.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = URI(""),
            value = 5.0
        )

        val quizItem3 = ComparisonQuizItem(
            title = "C",
            imgUri = URI(""),
            value = 8.0
        )

        val quizData = ComparisonQuizCore.QuizData(
            questions = listOf(quizItem1, quizItem2, quizItem3),
            currentQuestion = null,
            questionDescription = "Which country has more population?",
            comparisonMode = ComparisonMode.GREATER,
            category = FakeComparisonQuizData.generateCategory()
        )

        assertThat(quizData.questions).containsExactly(quizItem1, quizItem2, quizItem3)
        assertThat(quizData.currentQuestion).isNull()

        val nextQuizData = quizData.getNextQuestion()

        assertThat(nextQuizData.questions).containsExactly(quizItem3)
        assertThat(nextQuizData.questions).containsNoneOf(quizItem1, quizItem2)
        assertThat(nextQuizData.currentQuestion).isNotNull()
    }

    @Test
    fun `test nextQuestion when questions has size of 1 and current question is not null`() {
        val quizItem1 = ComparisonQuizItem(
            title = "A",
            imgUri = URI(""),
            value = 10.0
        )

        val quizItem2 = ComparisonQuizItem(
            title = "B",
            imgUri = URI(""),
            value = 5.0
        )

        val quizItem3 = ComparisonQuizItem(
            title = "C",
            imgUri = URI(""),
            value = 8.0
        )

        val quizData = ComparisonQuizCore.QuizData(
            questions = listOf(quizItem3),
            currentQuestion = ComparisonQuizQuestion(
                questions = quizItem1 to quizItem2,
                categoryId = "",
                comparisonMode = ComparisonMode.GREATER
            ),
            questionDescription = "Which country has more population?",
            comparisonMode = ComparisonMode.GREATER,
            category = FakeComparisonQuizData.generateCategory()
        )

        assertThat(quizData.questions).containsExactly(quizItem3)
        assertThat(quizData.currentQuestion).isNotNull()
        assertThat(quizData.currentQuestion?.questions?.first).isEqualTo(quizItem1)
        assertThat(quizData.currentQuestion?.questions?.second).isEqualTo(quizItem2)

        val nextQuizData = quizData.getNextQuestion()

        assertThat(nextQuizData.questions).isEmpty()
        assertThat(nextQuizData.questions).containsNoneOf(quizItem1, quizItem2, quizItem3)
        assertThat(nextQuizData.currentQuestion).isNotNull()
        assertThat(nextQuizData.currentQuestion?.questions?.first).isEqualTo(quizItem2)
        assertThat(nextQuizData.currentQuestion?.questions?.second).isEqualTo(quizItem3)
    }
}