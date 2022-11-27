package com.infinitepower.newquiz.domain.repository.wordle

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import kotlin.random.Random

interface WordleRepository {
    suspend fun getAllWords(): Set<String>

    fun generateRandomWord(
        quizType: WordleQuizType,
        random: Random = Random
    ): FlowResource<String>

    suspend fun  generateRandomTextWord(random: Random = Random): String

    suspend fun  generateRandomNumberWord(
        wordSize: Int = 5,
        random: Random = Random
    ): String

    fun isColorBlindEnabled(): FlowResource<Boolean>

    fun isLetterHintEnabled(): FlowResource<Boolean>

    fun isHardModeEnabled(): FlowResource<Boolean>

    suspend fun getWordleMaxRows(
        defaultMaxRow: Int? = null
    ): Int

    fun getAdRewardRowsToAdd(): Int

    fun validateWord(
        word: String,
        quizType: WordleQuizType
    ): Boolean
}