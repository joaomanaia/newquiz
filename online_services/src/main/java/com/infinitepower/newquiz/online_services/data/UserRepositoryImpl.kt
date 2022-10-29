package com.infinitepower.newquiz.online_services.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.core.common.database.DatabaseCommon
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.online_services.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val firestore = Firebase.firestore

    private val usersCol by lazy {
        firestore.collection(DatabaseCommon.UserCollection.NAME)
    }

    override suspend fun getUserByUid(uid: String): User? {
        val userDoc = usersCol.document(uid).get().await()
        return userDoc.toObject()
    }

    override suspend fun createUserDB(user: User) {
        val uid = user.uid ?: throw NullPointerException("User id is null")

        usersCol.document(uid).set(user).await()
    }
}