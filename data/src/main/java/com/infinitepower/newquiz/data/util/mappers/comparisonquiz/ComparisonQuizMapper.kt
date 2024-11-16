package com.infinitepower.newquiz.data.util.mappers.comparisonquiz

import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import java.net.URI

fun ComparisonQuizItem.toEntity() = ComparisonQuizItemEntity(
    title = title,
    value = value,
    imgUrl = imgUri.toASCIIString(),
)

fun ComparisonQuizItemEntity.toModel() = ComparisonQuizItem(
    title = title,
    value = value,
    imgUri = URI.create(imgUrl)
)
