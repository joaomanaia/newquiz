package com.infinitepower.newquiz.comparison_quiz.list

import androidx.annotation.Keep
import com.infinitepower.newquiz.domain.repository.home.HomeCategories
import com.infinitepower.newquiz.domain.repository.home.emptyHomeCategories
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory

@Keep
data class ComparisonQuizListScreenUiState(
    val homeCategories: HomeCategories<ComparisonQuizCategory> = emptyHomeCategories(),
    val selectedMode: ComparisonMode = ComparisonMode.GREATER,
    val internetConnectionAvailable: Boolean = true,
    val showCategoryConnectionInfo: ShowCategoryConnectionInfo = ShowCategoryConnectionInfo.NONE
)
