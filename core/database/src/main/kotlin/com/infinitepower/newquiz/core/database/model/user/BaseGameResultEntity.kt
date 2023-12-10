package com.infinitepower.newquiz.core.database.model.user

interface BaseGameResultEntity {
    val gameId: Int
    val earnedXp: Int
    val playedAt: Long
}