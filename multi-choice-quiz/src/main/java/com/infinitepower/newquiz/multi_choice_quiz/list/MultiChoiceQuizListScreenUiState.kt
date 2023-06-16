package com.infinitepower.newquiz.multi_choice_quiz.list

import androidx.annotation.Keep
import com.infinitepower.newquiz.domain.repository.home.HomeCategories
import com.infinitepower.newquiz.domain.repository.home.emptyHomeCategories
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory

@Keep
data class MultiChoiceQuizListScreenUiState(
    val savedQuestionsSize: Int = 0,
    val homeCategories: HomeCategories<MultiChoiceCategory> = emptyHomeCategories()
)