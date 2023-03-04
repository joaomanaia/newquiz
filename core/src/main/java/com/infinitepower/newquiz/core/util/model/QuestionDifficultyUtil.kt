package com.infinitepower.newquiz.core.util.model

import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.core.R as CoreR

fun QuestionDifficulty.getText(): UiText = when (this) {
    is QuestionDifficulty.Easy -> UiText.StringResource(CoreR.string.easy)
    is QuestionDifficulty.Medium -> UiText.StringResource(CoreR.string.medium)
    is QuestionDifficulty.Hard -> UiText.StringResource(CoreR.string.hard)
}