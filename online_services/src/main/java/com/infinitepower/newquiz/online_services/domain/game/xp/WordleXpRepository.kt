package com.infinitepower.newquiz.online_services.domain.game.xp

interface WordleXpRepository : XPRepository {
    fun getXpMultiplierFactor(rowsUsed: Int): Float

    fun generateRandomXP(rowsUsed: Int): Int
}