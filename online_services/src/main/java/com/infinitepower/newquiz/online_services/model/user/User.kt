package com.infinitepower.newquiz.online_services.model.user

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.common.DEFAULT_USER_PHOTO
import com.infinitepower.newquiz.core.util.kotlin.div
import com.infinitepower.newquiz.core.util.kotlin.pow
import com.infinitepower.newquiz.core.util.kotlin.roundToUInt
import kotlin.math.floor
import kotlin.math.sqrt

class UserNotLoggedInException : NullPointerException("User is not logged in")

@Keep
data class User(
    val uid: String,
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
        val totalXp: ULong = 0u,
        val diamonds: Int = 0,
        val multiChoiceQuizData: MultiChoiceQuizData = MultiChoiceQuizData(),
        val wordleData: WordleData = WordleData()
    ) {
        @Keep
        data class MultiChoiceQuizData(
            val totalQuestionsPlayed: ULong = 0u,
            val totalCorrectAnswers: ULong = 0u,
            val lastQuizTimes: List<Double> = emptyList()
        )

        @Keep
        data class WordleData(
            val wordsPlayed: ULong = 0u,
            val wordsCorrect: ULong = 0u
        )
    }

    init {
        require(uid.isNotBlank()) { "User uid is blank" }

        // Verify user info fields
        require(info.fullName.isNotBlank()) { "Full name is blank" }
        require(info.imageUrl.isNotBlank()) { "Image url is blank" }
    }

    val totalXp: ULong
        get() = data.totalXp

    val level: UInt
        get() = floor(sqrt(totalXp / 100.0)).roundToUInt()

    val averageQuizTime: Double
        get() = data.multiChoiceQuizData.lastQuizTimes.average()

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
        val newUser = copy(data = UserData(totalXp = newTotalXP))

        return newUser.level > level
    }

    fun getLevelAfter(newXp: ULong): UInt {
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
        totalXp = data.totalXp.toLong(),
        diamonds = data.diamonds,
        multiChoiceQuizData = UserEntity.UserData.MultiChoiceQuizData(
            totalQuestionsPlayed = data.multiChoiceQuizData.totalQuestionsPlayed.toLong(),
            totalCorrectAnswers = data.multiChoiceQuizData.totalCorrectAnswers.toLong(),
            lastQuizTimes = data.multiChoiceQuizData.lastQuizTimes,
        ),
        wordleData = UserEntity.UserData.WordleData(
            wordsPlayed = data.wordleData.wordsPlayed.toLong(),
            wordsCorrect = data.wordleData.wordsCorrect.toLong()
        )
    )
)