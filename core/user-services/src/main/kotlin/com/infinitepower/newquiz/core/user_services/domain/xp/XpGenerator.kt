package com.infinitepower.newquiz.core.user_services.domain.xp

import com.infinitepower.newquiz.model.question.QuestionDifficulty

interface XpGenerator {
    fun getDefaultXpForDifficulty(
        difficulty: QuestionDifficulty
    ): UInt
}
