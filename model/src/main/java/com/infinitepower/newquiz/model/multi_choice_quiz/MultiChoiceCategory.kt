package com.infinitepower.newquiz.model.multi_choice_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.GameMode
import com.infinitepower.newquiz.model.UiText

@Keep
data class MultiChoiceCategory(
    override val id: String,
    override val name: UiText,
    override val image: Any,
    override val requireInternetConnection: Boolean = true
) : BaseCategory {
    override val gameMode: GameMode = GameMode.MULTI_CHOICE
}

fun MultiChoiceCategory.toBaseCategory() = MultiChoiceBaseCategory.fromId(id)
