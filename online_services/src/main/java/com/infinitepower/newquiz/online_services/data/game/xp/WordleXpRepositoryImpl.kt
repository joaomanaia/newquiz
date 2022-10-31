package com.infinitepower.newquiz.online_services.data.game.xp

import com.infinitepower.newquiz.online_services.domain.game.xp.WordleXpRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

@Singleton
class WordleXpRepositoryImpl @Inject constructor() : WordleXpRepository {
    override fun randomXPRange(): IntRange = 10..20

    override fun generateRandomXP(range: IntRange): Int {
        return Random.nextInt(range)
    }

    override fun getXpMultiplierFactor(rowsUsed: Int) = 2.5f / rowsUsed

    override fun generateRandomXP(rowsUsed: Int): Int {
        val randomXPRange = randomXPRange()

        val randomXP = generateRandomXP(randomXPRange)

        val xpMultiplierFactor = getXpMultiplierFactor(rowsUsed)
        val xpMultiplied = randomXP * xpMultiplierFactor

        return xpMultiplied.roundToInt()
    }
}