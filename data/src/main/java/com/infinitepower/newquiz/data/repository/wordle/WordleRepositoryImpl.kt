package com.infinitepower.newquiz.data.repository.wordle

import android.content.Context
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.common.textWordleSupportedLang
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionRepository
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class WordleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val mathQuizCoreRepository: MathQuizCoreRepository,
    private val numberTriviaQuestionRepository: NumberTriviaQuestionRepository
) : WordleRepository {
    private val baseNumbers by lazy { 0..9 }

    override suspend fun getAllWords(): Set<String> = withContext(Dispatchers.IO) {
        val quizLanguage =
            settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)

        val listRawId = textWordleSupportedLang.find { lang ->
            lang.key == quizLanguage
        }?.rawListId ?: throw NullPointerException("Wordle language not found")

        val wordleListInputStream = context.resources.openRawResource(listRawId)

        try {
            wordleListInputStream
                .readBytes()
                .decodeToString()
                .split("\r\n", "\n")
                .filter { it.length == 5 }
                .map { it.uppercase().trim() }
                .toSet()
        } catch (e: Exception) {
            throw e
        } finally {
            wordleListInputStream.close()
        }
    }

    override fun generateRandomWord(
        quizType: WordleQuizType,
        random: Random
    ): FlowResource<WordleWord> = flow {
        try {
            emit(Resource.Loading())

            val randomWord = when (quizType) {
                WordleQuizType.TEXT -> generateRandomTextWord(random = random)
                WordleQuizType.NUMBER -> generateRandomNumberWord(random = random)
                WordleQuizType.MATH_FORMULA -> {
                    val formula = mathQuizCoreRepository.generateMathFormula(random = random)
                    WordleWord(formula.fullFormula)
                }

                WordleQuizType.NUMBER_TRIVIA -> numberTriviaQuestionRepository.generateWordleQuestion(
                    random = random
                )
            }

            emit(Resource.Success(randomWord))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "A error occurred while getting word."))
        }
    }

    override suspend fun generateRandomTextWord(random: Random): WordleWord {
        val allWords = getAllWords().shuffled(random)

        return WordleWord(allWords.random(random))
    }

    override suspend fun generateRandomTextWords(count: Int, random: Random): List<WordleWord> {
        val allWords = getAllWords().shuffled(random)

        return allWords
            .take(count)
            .map { word -> WordleWord(word) }
    }

    override suspend fun generateRandomNumberWord(
        wordSize: Int,
        random: Random
    ): WordleWord {
        val randomNumbers = List(wordSize) {
            baseNumbers.random(random)
        }

        return WordleWord(randomNumbers.joinToString(""))
    }

    override suspend fun isColorBlindEnabled(): Boolean {
        return settingsDataStoreManager.getPreference(SettingsCommon.WordleColorBlindMode)
    }

    override suspend fun isLetterHintEnabled(): Boolean {
        return settingsDataStoreManager.getPreference(SettingsCommon.WordleLetterHints)
    }

    override suspend fun isHardModeEnabled(): Boolean {
        return settingsDataStoreManager.getPreference(SettingsCommon.WordleHardMode)
    }

    override suspend fun getWordleMaxRows(defaultMaxRow: Int?): Int {
        if (defaultMaxRow == null) {
            // If is row limited return row limit value else return int max value
            val isRowLimited =
                settingsDataStoreManager.getPreference(SettingsCommon.WordleInfiniteRowsLimited)
            if (isRowLimited) return settingsDataStoreManager.getPreference(SettingsCommon.WordleInfiniteRowsLimit)

            return Int.MAX_VALUE
        }

        return defaultMaxRow
    }

    @Suppress("ReturnCount")
    override fun validateWord(word: String, quizType: WordleQuizType): Result<Unit> {
        if (word.isBlank()) return Result.failure(InvalidWordError.Empty)

        when (quizType) {
            WordleQuizType.TEXT -> {
                if (!word.all(Char::isLetter)) {
                    return Result.failure(InvalidWordError.NotOnlyLetters)
                }
            }

            WordleQuizType.NUMBER, WordleQuizType.NUMBER_TRIVIA -> {
                if (!word.all(Char::isDigit)) {
                    return Result.failure(InvalidWordError.NotOnlyDigits)
                }
            }

            WordleQuizType.MATH_FORMULA -> {
                if (!mathQuizCoreRepository.validateFormula(word)) {
                    return Result.failure(InvalidWordError.InvalidMathFormula)
                }
            }
        }

        return Result.success(Unit)
    }
}