package com.infinitepower.newquiz.compose.data.remote.user

interface UserApi {
    suspend fun createUser(user: User)

    suspend fun getUserByUid(uid: String): User?

    suspend fun tryAuthUpdateUserQuizXP(newXP: Long)
}