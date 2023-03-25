package com.infinitepower.newquiz.model.comparison_quiz

import android.net.Uri
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
data class ComparisonQuizItem(
    val title: String,
    val value: Double,
    val imgUri: Uri
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
        imgUri = Uri.parse(imgUrl)
    )
}
