package com.infinitepower.newquiz.online_services.domain.game.xp

interface XPRepository {
    fun randomXPRange(): IntRange

    fun generateRandomXP(range: IntRange): Int
}