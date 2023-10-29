package com.infinitepower.newquiz.comparison_quiz.ui

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCurrentQuestion
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizHelperValueState

@Keep
data class ComparisonQuizUiState(
    val currentQuestion: ComparisonQuizCurrentQuestion? = null,
    val gameCategory: ComparisonQuizCategory? = null,
    val comparisonMode: ComparisonMode? = null,
    val gameDescription: String? = null,
    val currentPosition: Int = 0,
    val highestPosition: Int = 0,
    val isGameOver: Boolean = false,
    val isSignedIn: Boolean = false,
    val userDiamonds: Int = -1,
    val userDiamondsLoading: Boolean = false,
    val skipCost: UInt = 0u,
    val firstItemHelperValueState: ComparisonQuizHelperValueState = ComparisonQuizHelperValueState.HIDDEN,
)
