package com.infinitepower.newquiz.model.comparison_quiz

import androidx.annotation.Keep
import java.net.URI

@Keep
data class ComparisonQuizItem(
    val title: String,
    val value: Double,
    val helperValue: String = value.toString(),
    val imgUri: URI
)
