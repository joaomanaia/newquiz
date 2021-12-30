package com.infinitepower.newquiz.compose.data.repository.user

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.compose.core.USERS_COLLECTION
import com.infinitepower.newquiz.compose.data.remote.user.User
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import kotlinx.coroutines.tasks.await

class UserApiImpl : UserApi {
    override suspend fun createUser(user: User) {
        val uid = user.uid ?: return

        Firebase
            .firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .set(user)
            .await()
    }

    override suspend fun getUserByUid(uid: String): User? = runCatching {
        Firebase
            .firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .get()
            .await()
    }.getOrNull()?.toObject()
}