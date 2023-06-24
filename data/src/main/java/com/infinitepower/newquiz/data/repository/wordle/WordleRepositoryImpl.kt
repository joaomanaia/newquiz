package com.infinitepower.newquiz.data.repository.wordle

import android.content.Context
import androidx.core.text.isDigitsOnly
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.common.dataStore.textWordleSupportedLang
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
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
        val quizLanguage = settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)

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
                    WordleWord(formula.fullFormulaWithoutSpaces)
                }
                WordleQuizType.NUMBER_TRIVIA -> numberTriviaQuestionRepository.generateWordleQuestion(random = random)
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

    override suspend fun generateRandomNumberWord(
        wordSize: Int,
        random: Random
    ): WordleWord {
        val randomNumbers = List(wordSize) {
            baseNumbers.random(random)
        }

        return WordleWord(randomNumbers.joinToString(""))
    }

    override fun isColorBlindEnabled(): FlowResource<Boolean> = flow {
        try {
            emit(Resource.Loading())

            val isColorBlindEnabled = settingsDataStoreManager.getPreference(SettingsCommon.WordleColorBlindMode)
            emit(Resource.Success(isColorBlindEnabled))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "A error occurred while checking if color blind is enabled."))
        }
    }

    override fun isLetterHintEnabled(): FlowResource<Boolean> = flow {
        try {
            emit(Resource.Loading())

            val isLetterHintEnabled = settingsDataStoreManager.getPreference(SettingsCommon.WordleLetterHints)
            emit(Resource.Success(isLetterHintEnabled))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "A error occurred while checking if letter hint is enabled."))
        }
    }

    override fun isHardModeEnabled(): FlowResource<Boolean> = flow {
        try {
            emit(Resource.Loading())

            val isHardModeEnabled = settingsDataStoreManager.getPreference(SettingsCommon.WordleHardMode)
            emit(Resource.Success(isHardModeEnabled))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "A error occurred while checking if hard mode is enabled."))
        }
    }

    override suspend fun getWordleMaxRows(defaultMaxRow: Int?): Int {
        if (defaultMaxRow == null) {
            // If is row limited return row limit value else return int max value
            val isRowLimited = settingsDataStoreManager.getPreference(SettingsCommon.WordleInfiniteRowsLimited)
            if (isRowLimited) return settingsDataStoreManager.getPreference(SettingsCommon.WordleInfiniteRowsLimit)

            return Int.MAX_VALUE
        }

        return defaultMaxRow
    }

    override fun validateWord(word: String, quizType: WordleQuizType): Boolean {
        return when (quizType) {
            WordleQuizType.TEXT -> word.isNotBlank()
            WordleQuizType.NUMBER, WordleQuizType.NUMBER_TRIVIA -> word.isDigitsOnly()
            WordleQuizType.MATH_FORMULA -> mathQuizCoreRepository.validateFormula(word)
        }
    }
}