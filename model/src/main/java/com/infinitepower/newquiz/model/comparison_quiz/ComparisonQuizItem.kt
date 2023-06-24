package com.infinitepower.newquiz.model.comparison_quiz

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import java.net.URI

@Keep
data class ComparisonQuizItem(
    val title: String,
    val value: Double,
    val imgUri: URI
)

@Keep
@Serializable
data class ComparisonQuizItemEntity(
    val title: String,
    val value: Double,
    val imgUrl: String
) : java.io.Serializable

fun ComparisonQuizItemEntity.toComparisonQuizItem(): ComparisonQuizItem {
    return ComparisonQuizItem(
        title = title,
        value = value,
        imgUri = URI.create(imgUrl)
    )
}
