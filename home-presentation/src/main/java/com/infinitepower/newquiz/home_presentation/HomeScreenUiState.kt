package com.infinitepower.newquiz.home_presentation

import androidx.annotation.Keep

@Keep
data class HomeScreenUiState(
    val isLoggedIn: Boolean = false,
    val showLoginCard: Boolean = true,
)