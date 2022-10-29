package com.infinitepower.newquiz.online_services.domain.user

import com.infinitepower.newquiz.online_services.model.user.User

interface UserRepository {
    suspend fun getUserByUid(uid: String): User?

    suspend fun createUserDB(user: User)
}