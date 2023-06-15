package com.infinitepower.newquiz.wordle.list

import androidx.annotation.Keep
import com.infinitepower.newquiz.domain.repository.home.HomeCategories
import com.infinitepower.newquiz.domain.repository.home.emptyHomeCategories
import com.infinitepower.newquiz.model.wordle.WordleCategory

@Keep
data class WordleListUiState(
    val homeCategories: HomeCategories<WordleCategory> = emptyHomeCategories()
)
