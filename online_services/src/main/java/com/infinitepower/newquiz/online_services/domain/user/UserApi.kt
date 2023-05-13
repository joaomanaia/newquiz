package com.infinitepower.newquiz.online_services.domain.user

import com.infinitepower.newquiz.online_services.model.user.UserEntity

interface UserApi {
    suspend fun getUserByUid(uid: String): UserEntity?

    suspend fun createUser(user: UserEntity)

    suspend fun updateLocalUser(newData: Map<String, Any>)

    suspend fun updateLocalUserTransaction(newData: (currentUser: UserEntity) -> Map<String, Any>)

    suspend fun updateLocalUser(
        newXp: ULong,
        averageQuizTime: Double,
        totalQuestionsPlayed: ULong,
        totalCorrectAnswers: ULong
    )
}