package com.infinitepower.newquiz.online_services.model.user

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.common.DEFAULT_USER_PHOTO

@Keep
data class  UserEntity(
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
        val wordleData: WordleData? = WordleData()
    ) {
        @Keep
        data class MultiChoiceQuizData(
            val totalQuestionsPlayed: Long? = 0,
            val totalCorrectAnswers: Long? = 0,
            val lastQuizTimes: List<Double>? = emptyList()
        )

        @Keep
        data class WordleData(
            val wordsPlayed: Long? = 0,
            val wordsCorrect: Long? = 0
        )
    }
}

internal fun UserEntity.toUser(): User = User(
    uid = uid ?: throw NullPointerException("User id is null"),
    info = User.UserInfo(
        fullName = info?.fullName ?: "NewQuiz User",
        imageUrl = info?.imageUrl ?: DEFAULT_USER_PHOTO
    ),
    data = User.UserData(
        totalXp = data?.totalXp?.toULong() ?: 0u,
        diamonds = data?.diamonds ?: 0,
        multiChoiceQuizData = User.UserData.MultiChoiceQuizData(
            totalQuestionsPlayed = data?.multiChoiceQuizData?.totalQuestionsPlayed?.toULong() ?: 0u,
            totalCorrectAnswers = data?.multiChoiceQuizData?.totalCorrectAnswers?.toULong() ?: 0u,
            lastQuizTimes = data?.multiChoiceQuizData?.lastQuizTimes ?: emptyList(),
        ),
        wordleData = User.UserData.WordleData(
            wordsPlayed = data?.wordleData?.wordsPlayed?.toULong() ?: 0u,
            wordsCorrect = data?.wordleData?.wordsCorrect?.toULong() ?: 0u
        )
    )
)