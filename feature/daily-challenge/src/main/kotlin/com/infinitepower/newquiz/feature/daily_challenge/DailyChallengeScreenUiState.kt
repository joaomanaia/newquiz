package com.infinitepower.newquiz.feature.daily_challenge

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask

@Keep
data class DailyChallengeScreenUiState(
    val loading: Boolean = true,
    val tasks: List<DailyChallengeTask> = emptyList(),
    val comparisonQuizCategories: List<ComparisonQuizCategory> = emptyList(),
    val userAvailable: Boolean = false
)
