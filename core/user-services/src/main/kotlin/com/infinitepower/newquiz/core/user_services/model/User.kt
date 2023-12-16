package com.infinitepower.newquiz.core.user_services.model

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.common.DEFAULT_USER_PHOTO
import com.infinitepower.newquiz.core.util.kotlin.div
import com.infinitepower.newquiz.core.util.kotlin.pow
import com.infinitepower.newquiz.core.util.kotlin.roundToUInt
import kotlin.math.floor
import kotlin.math.sqrt

@Keep
data class User(
    val uid: String,
    val fullName: String = "NewQuiz User",
    val imageUrl: String = DEFAULT_USER_PHOTO,
    val totalXp: ULong = 0u,
    val diamonds: UInt = 0u,
) {
    val level: UInt
        get() = floor(sqrt(totalXp / 100.0)).roundToUInt()

    fun getNextLevelXp(): UInt = getRequiredXpForLevel(level + 1u)

    private fun getRequiredXpForLevel(level: UInt): UInt = level.pow(2) * 100u

    fun getLevelProgress(): Float {
        val currentLevelXp = getRequiredXpForLevel(level)
        val nextLevelXp = getNextLevelXp()

        val requiredXp = nextLevelXp - currentLevelXp
        val currentXp = totalXp - currentLevelXp

        return currentXp.toFloat() / requiredXp.toFloat()
    }

    fun getRequiredXP(): ULong = getNextLevelXp() - totalXp

    fun isNewLevel(newXp: ULong): Boolean = getLevelAfter(newXp) > level

    fun getLevelAfter(newXp: ULong): UInt = copy(totalXp = totalXp + newXp).level
}
