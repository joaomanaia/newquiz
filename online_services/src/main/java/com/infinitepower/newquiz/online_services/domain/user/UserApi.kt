package com.infinitepower.newquiz.online_services.domain.user

import com.infinitepower.newquiz.online_services.model.user.UserEntity

interface UserApi {
    suspend fun getUserByUid(uid: String): UserEntity?

    suspend fun createUserDB(user: UserEntity)

    suspend fun updateLocalUserNewXPWordle(
        newXp: Long,
        wordsPlayed: Long,
        wordsCorrect: Long
    )

    suspend fun updateLocalUserNewXP(
        newXp: Long,
        averageQuizTime: Double,
        totalQuestionsPlayed: Long,
        totalCorrectAnswers: Long
    )

    suspend fun updateLocalUserDiamonds(n: Int)
}