package com.infinitepower.newquiz.online_services.model.user

import androidx.annotation.Keep
import kotlin.math.*

@Keep
data class User(
    val uid: String? = null,
    val info: UserInfo? = UserInfo(),
    val data: UserData? = UserData()
) {
    @Keep
    data class UserInfo(
        val fullName: String? = null,
        val imageUrl: String? = null
    )

    @Keep
    data class UserData(
        val totalXp: Long? = 0,
        val diamonds: Int? = 0,
        val multiChoiceQuizData: MultiChoiceQuizData? = MultiChoiceQuizData(),
    ) {
        @Keep
        data class MultiChoiceQuizData(
            val totalQuestionsPlayed: Long? = 0,
            val totalCorrectAnswers: Long? = 0,
            val averageQuizTime: Double? = 0.0,
            val lastQuizTimes: List<Double>? = emptyList()
        )
    }

    val totalXp: Long
        get() = data?.totalXp ?: 0

    val level: Int
        get() = floor(sqrt(totalXp / 100.0)).roundToInt()

    fun getNextLevelXp(): Long {
        val nextLevel = level + 1
        val nextLevelXP = nextLevel.pow(2) * 100

        return nextLevelXP.roundToLong()
    }

    fun getLevelProgress(): Float {
        return totalXp.toFloat() / getNextLevelXp()
    }

    fun getRequiredXP(): Long = getNextLevelXp() - totalXp

    private infix fun Int.pow(n: Int): Double = toDouble().pow(n)
}