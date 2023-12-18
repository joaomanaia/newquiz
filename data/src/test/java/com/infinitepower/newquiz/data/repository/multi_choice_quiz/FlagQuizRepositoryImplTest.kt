package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.data.repository.country.TestCountryRepositoryImpl
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [FlagQuizRepositoryImpl]
 */
internal class FlagQuizRepositoryImplTest {
    private lateinit var repository: FlagQuizRepositoryImpl

    @BeforeTest
    fun setUp() {
        repository = FlagQuizRepositoryImpl(
            countryRepository = TestCountryRepositoryImpl()
        )
    }

    @Test
    fun `getFlagQuiz() returns a list of questions`() = runTest {
        val questionSize = 5

        val questions = repository.getRandomQuestions(
            amount = questionSize,
            category = MultiChoiceBaseCategory.Flag
        )

        assertThat(questions).hasSize(questionSize)

        // Use imageUrl because it is unique for each question
        val uniqueQuestions = questions.distinctBy { it.imageUrl }
        assertThat(uniqueQuestions).hasSize(questionSize)

        // Check if the questions category is correct
        assertThat(questions.all { it.category == MultiChoiceBaseCategory.Flag }).isTrue()
    }
}