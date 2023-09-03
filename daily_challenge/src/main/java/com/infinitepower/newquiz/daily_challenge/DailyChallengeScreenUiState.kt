package com.infinitepower.newquiz.daily_challenge

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask

@Keep
data class DailyChallengeScreenUiState(
    val tasks: List<DailyChallengeTask> = emptyList(),
    val comparisonQuizCategories: List<ComparisonQuizCategory> = emptyList(),
    val userSignedIn: Boolean = false
)
