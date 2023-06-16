package com.infinitepower.newquiz.model.wordle

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.UiText

@Keep
data class WordleCategory(
    val wordleQuizType: WordleQuizType,
    override val id: String = wordleQuizType.name,
    override val name: UiText,
    override val image: Any,
    override val requireInternetConnection: Boolean = false
) : BaseCategory
