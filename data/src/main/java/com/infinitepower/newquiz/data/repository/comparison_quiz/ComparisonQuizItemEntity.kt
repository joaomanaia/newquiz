package com.infinitepower.newquiz.data.repository.comparison_quiz

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ComparisonQuizItemEntity(
    val title: String,
    val value: Double,
    val imgUrl: String
) : java.io.Serializable
