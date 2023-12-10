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

    fun getNextLevelXp(): UInt {
        val nextLevel = level + 1u

        return nextLevel.pow(2) * 100u
    }

    fun getLevelProgress(): Float {
        return totalXp / getNextLevelXp().toFloat()
    }

    fun getRequiredXP(): ULong = getNextLevelXp() - totalXp

    fun isNewLevel(newXp: ULong): Boolean {
        val newTotalXP = totalXp + newXp
        val newUser = copy(totalXp = newTotalXP)

        return newUser.level > level
    }

    fun getLevelAfter(newXp: ULong): UInt {
        val newTotalXP = totalXp + newXp
        val newUser = copy(totalXp = newTotalXP)

        return newUser.level
    }
}
