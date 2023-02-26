package com.infinitepower.newquiz.wordle.data.repository.wordle

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeWordleRepository @Inject constructor() : WordleRepository {
    private val baseNumbers by lazy { 0..9 }

    private val allWords = setOf("TEST")

    override suspend fun getAllWords(random: Random): Set<String> = allWords.shuffled(random).toSet()

    override fun generateRandomWord(
        quizType: WordleQuizType,
        random: Random
    ): FlowResource<WordleWord> = flow {
        try {
            emit(Resource.Loading())

            val randomWord = when (quizType) {
                WordleQuizType.TEXT -> generateRandomTextWord(random = random)
                WordleQuizType.NUMBER -> generateRandomNumberWord(random = random)
                WordleQuizType.MATH_FORMULA -> generateRandomNumberWord(random = random)
                WordleQuizType.NUMBER_TRIVIA -> generateRandomNumberWord(random = random)
            }

            emit(Resource.Success(randomWord))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "A error occurred while getting word"))
        }
    }

    override suspend fun generateRandomTextWord(random: Random): WordleWord {
        val allWords = getAllWords()
        return WordleWord(allWords.random())
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

    override fun isColorBlindEnabled(): FlowResource<Boolean> = flowOf(Resource.Success(false))

    override fun isLetterHintEnabled(): FlowResource<Boolean> = flowOf(Resource.Success(false))

    override fun isHardModeEnabled(): FlowResource<Boolean> = flowOf(Resource.Success(false))

    override suspend fun animationsEnabled(): Boolean = true

    override suspend fun getWordleMaxRows(defaultMaxRow: Int?): Int = defaultMaxRow ?: Int.MAX_VALUE

    override fun getAdRewardRowsToAdd(): Int = 1
    override fun validateWord(word: String, quizType: WordleQuizType): Boolean = true
}