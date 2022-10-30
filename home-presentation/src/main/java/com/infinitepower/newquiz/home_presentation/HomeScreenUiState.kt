package com.infinitepower.newquiz.home_presentation

import androidx.annotation.Keep

@Keep
data class HomeScreenUiState(
    val isLoggedIn: Boolean = false,
    val showLoginCard: Boolean = false,
    val recommendedHomeGame: RecommendedHomeGame = RecommendedHomeGame.NO_GAME
)

enum class RecommendedHomeGame {
    NO_GAME,
    QUICK_MULTICHOICEQUIZ,
    WORDLE_INFINITE,
    FLAG,
    LOGO
}