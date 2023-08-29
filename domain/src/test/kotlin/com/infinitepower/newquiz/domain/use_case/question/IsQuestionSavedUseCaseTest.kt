package com.infinitepower.newquiz.domain.use_case.question

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [IsQuestionSavedUseCase].
 */
internal class IsQuestionSavedUseCaseTest {
    private val savedQuestionRepository: SavedMultiChoiceQuestionsRepository = mockk()

    private lateinit var isQuestionSavedUseCase: IsQuestionSavedUseCase

    @BeforeTest
    fun setUp() {
        isQuestionSavedUseCase = IsQuestionSavedUseCase(savedQuestionRepository)
    }

    @Test
    fun `should return Resource#Success(true) when question is saved`() = runTest {
        val questions = List(5) { getBasicMultiChoiceQuestion(it) }

        every { savedQuestionRepository.getFlowQuestions() } returns flowOf(questions)

        isQuestionSavedUseCase(questions.first()).test {
            assertThat(awaitItem()).isEqualTo(Resource.Loading(null))
            assertThat(awaitItem()).isEqualTo(Resource.Success(true))
            awaitComplete()
        }
    }

    @Test
    fun `should return Resource#Success(false) when question is not saved`() = runTest {
        val questions = List(5) { getBasicMultiChoiceQuestion(it) }

        every { savedQuestionRepository.getFlowQuestions() } returns flowOf(questions)

        isQuestionSavedUseCase(getBasicMultiChoiceQuestion(-1)).test {
            assertThat(awaitItem()).isEqualTo(Resource.Loading(null))
            assertThat(awaitItem()).isEqualTo(Resource.Success(false))
            awaitComplete()
        }
    }

    @Test
    fun `should return Resource#Success(false) when questions saved are empty`() = runTest {
        every { savedQuestionRepository.getFlowQuestions() } returns flowOf(emptyList())

        isQuestionSavedUseCase(getBasicMultiChoiceQuestion(-1)).test {
            assertThat(awaitItem()).isEqualTo(Resource.Loading(null))
            assertThat(awaitItem()).isEqualTo(Resource.Success(false))
            awaitComplete()
        }
    }

    @Test
    fun `should return Resource#Error when an exception occurs`() = runTest {
        val exception = Exception("An error occurred...")

        every { savedQuestionRepository.getFlowQuestions() } throws exception

        isQuestionSavedUseCase(getBasicMultiChoiceQuestion(-1)).test {
            assertThat(awaitItem()).isEqualTo(Resource.Loading(null))
            assertThat(awaitItem()).isEqualTo(Resource.Error(exception.localizedMessage, null))
            awaitComplete()
        }
    }
}
