package com.infinitepower.newquiz.compose.data.remote.user

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class FakeUserApiImpl : UserApi {

    private val users = MutableStateFlow<List<User>>(emptyList())

    override suspend fun createUser(user: User) {
        users.emit(
            users.first().toMutableList().apply {
                add(user)
            }
        )
    }

    override suspend fun getUserByUid(uid: String) = users.first().find {
        it.uid == uid
    }
}