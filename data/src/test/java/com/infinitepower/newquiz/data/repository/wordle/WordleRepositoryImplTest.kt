package com.infinitepower.newquiz.data.repository.wordle

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionRepository
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [WordleRepositoryImpl]
 */
internal class WordleRepositoryImplTest {
    private lateinit var repository: WordleRepositoryImpl

    private val context = mockk<Context>(relaxed = true)
    private val settingsDataStoreManager = mockk<DataStoreManager>()
    private val mathQuizCoreRepository = mockk<MathQuizCoreRepository>()
    private val numberTriviaQuestionRepository = mockk<NumberTriviaQuestionRepository>()

    @BeforeTest
    fun setup() {
        repository = WordleRepositoryImpl(
            context = context,
            settingsDataStoreManager = settingsDataStoreManager,
            mathQuizCoreRepository = mathQuizCoreRepository,
            numberTriviaQuestionRepository = numberTriviaQuestionRepository
        )
    }

    @AfterTest
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getAllWords should return a list of words`() = runTest {
        coEvery {
            settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)
        } returns "en"
        mockkWordsResources()

        val words = repository.getAllWords()
        assertThat(words).isNotEmpty()
        assertThat(words).hasSize(3)

        coVerify(exactly = 1) {
            settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)
            context.resources.openRawResource(any())
        }
        confirmVerified()
    }

    @Test
    fun `getAllWords should throw an exception when language is not supported`() = runTest {
        coEvery {
            settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)
        } returns "a"
        mockkWordsResources()

        assertThrows<NullPointerException> {
            repository.getAllWords()
        }

        coVerify(exactly = 1) {
            settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)
        }
        confirmVerified()
    }

    @Test
    fun `generateRandomTextWord should return a random word`() = runTest {
        coEvery {
            settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)
        } returns "en"
        mockkWordsResources()

        val word = repository.generateRandomTextWord()

        coVerify(exactly = 1) {
            settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)
            context.resources.openRawResource(any())
        }
        confirmVerified()

        assertThat(word.word).isNotEmpty()
        assertThat(allWords).contains(word.word.lowercase())
    }

    @CsvSource(
        "word,TEXT",
        "123,NUMBER",
        "123,NUMBER_TRIVIA",
        "1+2=3,MATH_FORMULA"
    )
    @ParameterizedTest
    fun `validateWord should return true when word is valid`(
        word: String,
        quizType: WordleQuizType
    ) = runTest {
        every { mathQuizCoreRepository.validateFormula(any()) } returns true
        val result = repository.validateWord(word, quizType)

        assertThat(result.isSuccess).isTrue()
    }

    private val allWords = listOf("word1", "word2", "word3", "wordignored")

    private fun mockkWordsResources() {
        val words = allWords.joinToString("\n")
        every { context.resources.openRawResource(any()) } returns words.byteInputStream()
    }
}
