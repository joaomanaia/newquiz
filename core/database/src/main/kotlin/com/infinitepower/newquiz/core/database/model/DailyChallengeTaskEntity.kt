package com.infinitepower.newquiz.core.database.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "daily_challenge_tasks")
data class DailyChallengeTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val diamondsReward: Int,
    val experienceReward: Int,
    val isClaimed: Boolean,
    val currentValue: Int,
    val maxValue: Int,
    val type: String,
    val startDate: Long,
    val endDate: Long
)
