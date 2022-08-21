package com.infinitepower.newquiz.model.multi_choice_quiz

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes

@Keep
data class MultiChoiceQuestionCategory(
    val id: Int,
    @StringRes val name: Int,
    @DrawableRes val image: Int? = null
)