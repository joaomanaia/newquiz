package com.infinitepower.newquiz.domain.repository.wordle

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import kotlin.random.Random

interface WordleRepository {
    suspend fun getAllWords(random: Random = Random): Set<String>

    fun generateRandomWord(
        quizType: WordleQuizType,
        random: Random = Random
    ): FlowResource<WordleWord>

    suspend fun generateRandomTextWord(random: Random = Random): WordleWord

    suspend fun generateRandomNumberWord(
        wordSize: Int = 5,
        random: Random = Random
    ): WordleWord

    fun isColorBlindEnabled(): FlowResource<Boolean>

    fun isLetterHintEnabled(): FlowResource<Boolean>

    fun isHardModeEnabled(): FlowResource<Boolean>

    suspend fun getWordleMaxRows(
        defaultMaxRow: Int? = null
    ): Int

    fun validateWord(
        word: String,
        quizType: WordleQuizType
    ): Boolean
}