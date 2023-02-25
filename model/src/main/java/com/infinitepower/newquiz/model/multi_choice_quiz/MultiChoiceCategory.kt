package com.infinitepower.newquiz.model.multi_choice_quiz

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.infinitepower.newquiz.model.UiText

@Keep
data class MultiChoiceCategory(
    val key: String,
    val id: Int,
    val name: UiText,
    @DrawableRes val image: Int
)

fun MultiChoiceCategory.toBaseCategory() = MultiChoiceBaseCategory.fromKey(key)
