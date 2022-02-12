package com.infinitepower.newquiz.compose.data.repository.user

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.compose.core.FirebaseCommon
import com.infinitepower.newquiz.compose.core.USERS_COLLECTION
import com.infinitepower.newquiz.compose.core.util.quiz.QuizXPUtil
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.data.remote.user.User
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import kotlinx.coroutines.tasks.await

class UserApiImpl(
    private val authUserApi: AuthUserApi,
    private val userXPUtil: QuizXPUtil
) : UserApi {

    private fun getAuthUserRef() = authUserApi.getUid()?.let { uid ->
        Firebase
            .firestore
            .collection(USERS_COLLECTION)
            .document(uid)
    }


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

    override suspend fun tryAuthUpdateUserQuizXP(newXP: Long) {
        val authUserRef = getAuthUserRef() ?: return

        val firestore = Firebase.firestore
        firestore.runTransaction { transition ->
            val userSnap = transition.get(authUserRef)

            if (userSnap.exists()) {
                val userTotalXP = userSnap.getLong(FirebaseCommon.Users.UserData.UserXp.TOTAL_XP_FIELD) ?: 0
                val currentLevel = userSnap.getLong(FirebaseCommon.Users.UserData.LEVEL_FIELD) ?: 0

                val newUserMap = mutableMapOf<String, Any>(
                    FirebaseCommon.Users.UserData.UserXp.TOTAL_XP_FIELD to FieldValue.increment(newXP)
                )

                val hasNewLevel = userXPUtil.verifyNewLevel(currentLevel, userTotalXP + newXP)
                if (hasNewLevel) {
                    newUserMap[FirebaseCommon.Users.UserData.UserXp.XP_FIELD] = 0L
                    newUserMap[FirebaseCommon.Users.UserData.LEVEL_FIELD] = FieldValue.increment(1)
                } else {
                    newUserMap[FirebaseCommon.Users.UserData.UserXp.XP_FIELD] = FieldValue.increment(newXP)
                }

                transition.update(authUserRef, newUserMap)
            }

            null
        }.await()
    }
}