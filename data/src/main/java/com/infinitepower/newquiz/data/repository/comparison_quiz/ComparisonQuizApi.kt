package com.infinitepower.newquiz.data.repository.comparison_quiz

import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import kotlin.random.Random

interface ComparisonQuizApi {
    suspend fun generateQuestions(
        category: ComparisonQuizCategory,
        size: Int,
        random: Random = Random
    ): List<ComparisonQuizItemEntity>
}
