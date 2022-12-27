package com.infinitepower.newquiz.online_services.model.user

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.common.DEFAULT_USER_PHOTO
import kotlin.math.*

@Keep
data class User(
    val uid: String? = null,
    val info: UserInfo = UserInfo(),
    val data: UserData = UserData()
) {
    @Keep
    data class UserInfo(
        val fullName: String = "NewQuiz User",
        val imageUrl: String = DEFAULT_USER_PHOTO
    )

    @Keep
    data class UserData(
        val totalXp: Long = 0,
        val diamonds: Int = 0,
        val multiChoiceQuizData: MultiChoiceQuizData? = MultiChoiceQuizData(),
        val wordleData: WordleData? = WordleData()
    ) {
        @Keep
        data class MultiChoiceQuizData(
            val totalQuestionsPlayed: Long = 0,
            val totalCorrectAnswers: Long = 0,
            val lastQuizTimes: List<Double> = emptyList()
        )

        @Keep
        data class WordleData(
            val wordsPlayed: Long = 0,
            val wordsCorrect: Long = 0
        )
    }

    val totalXp: Long
        get() = data.totalXp

    val level: Int
        get() = floor(sqrt(totalXp / 100.0)).roundToInt()

    val averageQuizTime: Double
        get() = data.multiChoiceQuizData?.lastQuizTimes?.average() ?: 0.0

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

    fun isNewLevel(newXp: Long): Boolean {
        val newTotalXP = totalXp + newXp
        val newUser = copy(data = UserData(totalXp = newTotalXP))

        return newUser.level > level
    }

    fun getLevelAfter(newXp: Long): Int {
        val newTotalXP = totalXp + newXp
        val newUser = copy(data = UserData(totalXp = newTotalXP))

        return newUser.level
    }
}

internal fun User.toUserEntity(): UserEntity = UserEntity(
    uid = uid,
    info = UserEntity.UserInfo(
        fullName = info.fullName,
        imageUrl = info.imageUrl
    ),
    data = UserEntity.UserData(
        totalXp = data.totalXp,
        diamonds = data.diamonds,
        multiChoiceQuizData = UserEntity.UserData.MultiChoiceQuizData(
            totalQuestionsPlayed = data.multiChoiceQuizData?.totalQuestionsPlayed,
            totalCorrectAnswers = data.multiChoiceQuizData?.totalCorrectAnswers,
            lastQuizTimes = data.multiChoiceQuizData?.lastQuizTimes,
        ),
        wordleData = UserEntity.UserData.WordleData(
            wordsPlayed = data.wordleData?.wordsPlayed,
            wordsCorrect = data.wordleData?.wordsCorrect
        )
    )
)