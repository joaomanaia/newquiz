package com.infinitepower.newquiz.model.multi_choice_quiz

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.infinitepower.newquiz.model.UiText

@Keep
data class MultiChoiceQuestionCategory(
    val id: Int,
    val name: UiText,
    @DrawableRes val image: Int? = null
)