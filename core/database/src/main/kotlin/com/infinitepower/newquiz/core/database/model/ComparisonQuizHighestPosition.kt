package com.infinitepower.newquiz.core.database.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "comparison_quiz_highest_position")
data class ComparisonQuizHighestPosition(
    @PrimaryKey val categoryId: String,
    val highestPosition: Int
)
