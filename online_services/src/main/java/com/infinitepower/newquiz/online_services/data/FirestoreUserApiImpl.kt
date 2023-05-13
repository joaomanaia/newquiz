package com.infinitepower.newquiz.online_services.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.core.common.database.DatabaseCommon
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.model.config.RemoteConfigApi
import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.model.user.UserEntity
import com.infinitepower.newquiz.online_services.model.user.toUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserApiImpl @Inject constructor(
    private val authUserRepository: AuthUserRepository,
    private val remoteConfigApi: RemoteConfigApi
) : UserApi {
    private val firestore = Firebase.firestore

    private val usersCol by lazy {
        firestore.collection(DatabaseCommon.UserCollection.NAME)
    }

    override suspend fun getUserByUid(uid: String): UserEntity? {
        val userDoc = usersCol.document(uid).get().await()
        return userDoc.toObject()
    }

    override suspend fun createUser(user: UserEntity) {
        val uid = user.uid ?: throw NullPointerException("User id is null")

        usersCol.document(uid).set(user).await()
    }

    override suspend fun updateLocalUser(newData: Map<String, Any>) {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        usersCol
            .document(localUid)
            .update(newData)
            .await()
    }

    override suspend fun updateLocalUserTransaction(
        newData: (currentUser: UserEntity) -> Map<String, Any>
    ) {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        val userDoc = usersCol.document(localUid)

        firestore.runTransaction { transition ->
            // Check if the local user exists and get it
            val user = transition.get(userDoc).toObject<UserEntity>() ?: throw FirebaseFirestoreException(
                "User does not exist",
                FirebaseFirestoreException.Code.ABORTED
            )

            val newUserUpdateMap = newData(user)

            transition.update(userDoc, newUserUpdateMap)
        }.await()
    }

    override suspend fun updateLocalUser(
        newXp: ULong,
        averageQuizTime: Double,
        totalQuestionsPlayed: ULong,
        totalCorrectAnswers: ULong
    ) {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        val userDoc = usersCol.document(localUid)

        firestore.runTransaction { transition ->
            val user = transition.get(userDoc).toObject<UserEntity>() ?: throw FirebaseFirestoreException(
                "User does not exist",
                FirebaseFirestoreException.Code.ABORTED
            )

            val lastQuizTimes = user.data?.multiChoiceQuizData?.lastQuizTimes.orEmpty()

            if (lastQuizTimes.size >= 5) {
                transition.update(
                    userDoc,
                    DatabaseCommon.UserCollection.FIELD_LAST_QUIZ_TIMES,
                    FieldValue.arrayRemove(lastQuizTimes.first())
                )
            }

            val newUserUpdateMap = mapOf(
                DatabaseCommon.UserCollection.FIELD_TOTAL_XP to FieldValue.increment(newXp.toLong()),
                DatabaseCommon.UserCollection.FIELD_LAST_QUIZ_TIMES to FieldValue.arrayUnion(averageQuizTime),
                DatabaseCommon.UserCollection.FIELD_TOTAL_QUESTIONS_PLAYED to FieldValue.increment(totalQuestionsPlayed.toLong()),
                DatabaseCommon.UserCollection.FIELD_CORRECT_ANSWERS to FieldValue.increment(totalCorrectAnswers.toLong()),
            )

            if (user.toUser().isNewLevel(newXp)) {
                val newUserMapWithDiamonds = getNewUserLevelMapWithDiamonds(remoteConfigApi, user.toUser(), newXp)

                transition.update(userDoc, newUserUpdateMap + newUserMapWithDiamonds)
            } else {
                transition.update(userDoc, newUserUpdateMap)
            }
        }.await()
    }
}