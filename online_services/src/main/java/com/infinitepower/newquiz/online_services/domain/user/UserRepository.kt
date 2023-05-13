package com.infinitepower.newquiz.online_services.domain.user

import com.infinitepower.newquiz.online_services.model.user.User

interface UserRepository {
    suspend fun getUserByUid(uid: String): User?

    suspend fun getLocalUser(): User?

    suspend fun createUser(user: User)

    suspend fun updateLocalUser(
        newXp: ULong,
        wordsPlayed: ULong,
        wordsCorrect: ULong
    )

    suspend fun updateLocalUser(
        newXp: ULong,
        averageQuizTime: Double,
        totalQuestionsPlayed: ULong,
        totalCorrectAnswers: ULong
    )

    suspend fun addLocalUserDiamonds(diamonds: Int)
}