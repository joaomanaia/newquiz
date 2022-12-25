package com.infinitepower.newquiz.model.wordle.daily

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class WordleDailyItem(
    val date: String,
    val words: Map<String, String>
)  : java.io.Serializable