package com.infinitepower.newquiz.model.wordle

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.UiText

@Keep
data class WordleCategory(
    override val id: String,
    override val name: UiText,
    override val image: Any,
    override val requireInternetConnection: Boolean = false,
    val wordleQuizType: WordleQuizType
) : BaseCategory
