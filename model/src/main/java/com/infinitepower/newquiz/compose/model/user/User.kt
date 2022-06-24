package com.infinitepower.newquiz.compose.model.user

import androidx.annotation.Keep
import java.util.*

@Keep
data class User(
    val uid: String? = null,
    val info: UserInfo? = null,
    val data: UserData? = UserData()
) {
    @Keep
    data class UserInfo(
        val fullname: String? = null,
        val imageUrl: String? = null,
        val location: String? = Locale.getDefault().country
    )

    @Keep
    data class UserData(
        val userXp: UserXp? = UserXp(),
        val level: Int? = 0,
        val money: UserMoney? = UserMoney(),
        val gameData: UserGameData? = UserGameData(),
    ) {
        @Keep
        data class UserMoney(
            val coins: Int? = 0,
            val diamonds: Int? = 0,
        )

        @Keep
        data class UserXp(
            val xp: Long? = 0,
            val totalXp: Long? = 0,
        )

        @Keep
        data class UserGameData(
            val totalGameData: UserTotalGameData? = UserTotalGameData(),
            val averageQuizTime: Double? = 0.0,
            val lastQuizTimes: List<Double>? = emptyList()
        ) {
            @Keep
            data class UserTotalGameData(
                val totalGamesPlayed: Int? = 0,
                val totalCorrectAnswers: Int? = 0,
            )
        }
    }
}