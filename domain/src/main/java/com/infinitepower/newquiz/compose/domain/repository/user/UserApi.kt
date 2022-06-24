package com.infinitepower.newquiz.compose.domain.repository.user

import com.infinitepower.newquiz.compose.model.user.User

interface UserApi {
    suspend fun createUser(user: User)

    suspend fun getUserByUid(uid: String): User?

    suspend fun tryAuthUpdateUserQuizXP(newXP: Long)
}