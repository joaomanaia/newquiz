package com.infinitepower.newquiz.online_services.domain.user

import com.infinitepower.newquiz.online_services.model.user.User

interface UserRepository {
    suspend fun getUserByUid(uid: String): User?

    suspend fun getLocalUser(): User?

    suspend fun createUserDB(user: User)

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