package com.infinitepower.newquiz.online_services.model.user

import androidx.annotation.Keep

@Keep
data class User(
    val uid: String? = null,
    val info: UserInfo? = null,
    val data: UserData? = UserData()
) {
    @Keep
    data class UserInfo(
        val fullName: String? = null,
        val imageUrl: String? = null
    )

    @Keep
    data class UserData(
        val userXp: UserXp? = UserXp(),
        val level: Int? = 0,
        val diamonds: Int? = 0,
        val multiChoiceQuizData: MultiChoiceQuizData? = MultiChoiceQuizData(),
    ) {
        @Keep
        data class UserXp(
            val currentXp: Long? = 0,
            val totalXp: Long? = 0,
        )

        @Keep
        data class MultiChoiceQuizData(
            val totalGameData: TotalGameData? = TotalGameData(),
            val averageQuizTime: Double? = 0.0,
            val lastQuizTimes: List<Double>? = emptyList()
        ) {
            @Keep
            data class TotalGameData(
                val totalGamesPlayed: Int? = 0,
                val totalCorrectAnswers: Int? = 0,
            )
        }
    }
}