package com.infinitepower.newquiz.compose.data.repository.user

import com.infinitepower.newquiz.compose.data.remote.user.User
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class FakeUserApiImpl : UserApi {

    private val users = MutableStateFlow<List<User>>(emptyList())

    override suspend fun createUser(user: User) {
        users.first().toMutableList().apply {
            add(user)
        }.also {
            users.emit(it)
        }
    }

    override suspend fun getUserByUid(uid: String) = users.first().find { it.uid == uid }

    override suspend fun tryAuthUpdateUserQuizXP(newXP: Long) {}
}