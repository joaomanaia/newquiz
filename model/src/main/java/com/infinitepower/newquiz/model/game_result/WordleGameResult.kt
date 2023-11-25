package com.infinitepower.newquiz.model.game_result

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.datetime.LocalDateTime

@Keep
data class WordleGameResult(
    val earnedXp: UInt,
    val playedAt: LocalDateTime,
    val rowsUsed: UInt,
    val category: String,
    val difficulty: QuestionDifficulty,
)