package com.infinitepower.newquiz.core.user_services.data.xp

import com.infinitepower.newquiz.core.user_services.domain.xp.WordleXpGenerator
import com.infinitepower.newquiz.core.util.kotlin.roundToUInt
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class WordleXpGeneratorImpl @Inject constructor() : WordleXpGenerator {
    override fun generateXp(rowsUsed: UInt): UInt {
        val defaultXp = getDefaultXp()

        // Calculate the XP multiplier based on the number of rows used.
        // The XP multiplier is inversely proportional to the square root of the number of rows used.
        // This means that the XP awarded decreases as the number of rows used increases.
        val xpMultiplier = (2 / sqrt(rowsUsed.toDouble()))

        return (defaultXp.toInt() * xpMultiplier).roundToUInt()
    }
}