package com.infinitepower.newquiz.online_services.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.core.analytics.logging.CoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.database.DatabaseCommon
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.model.user.UserEntity
import com.infinitepower.newquiz.online_services.model.user.toUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserApiImpl @Inject constructor(
    private val authUserRepository: AuthUserRepository,
    private val coreLoggingAnalytics: CoreLoggingAnalytics
) : UserApi {
    private val firestore = Firebase.firestore

    private val usersCol by lazy {
        firestore.collection(DatabaseCommon.UserCollection.NAME)
    }

    private val remoteConfig by lazy { Firebase.remoteConfig }

    override suspend fun getUserByUid(uid: String): UserEntity? {
        val userDoc = usersCol.document(uid).get().await()
        return userDoc.toObject()
    }

    override suspend fun createUserDB(user: UserEntity) {
        val uid = user.uid ?: throw NullPointerException("User id is null")

        usersCol.document(uid).set(user).await()
    }

    override suspend fun updateLocalUserNewXPWordle(
        newXp: Long,
        wordsPlayed: Long,
        wordsCorrect: Long
    ) {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        val userDoc = usersCol.document(localUid)

        firestore.runTransaction { transition ->
            val user = transition.get(userDoc).toObject<UserEntity>() ?: throw FirebaseFirestoreException(
                "User does not exist",
                FirebaseFirestoreException.Code.ABORTED
            )

            val newUserUpdateMap = mapOf(
                DatabaseCommon.UserCollection.FIELD_TOTAL_XP to FieldValue.increment(newXp),
                DatabaseCommon.UserCollection.FIELD_WORDLE_WORDS_PLAYED to FieldValue.increment(wordsPlayed),
                DatabaseCommon.UserCollection.FIELD_WORDLE_WORDS_CORRECT to FieldValue.increment(wordsCorrect),
            )

            val newUserMapWithDiamonds = getNewUserMapWithDiamonds(user, newXp)

            transition.update(userDoc, newUserUpdateMap + newUserMapWithDiamonds)
        }.await()
    }

    override suspend fun updateLocalUserNewXP(
        newXp: Long,
        averageQuizTime: Double,
        totalQuestionsPlayed: Long,
        totalCorrectAnswers: Long
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
                DatabaseCommon.UserCollection.FIELD_TOTAL_XP to FieldValue.increment(newXp),
                DatabaseCommon.UserCollection.FIELD_LAST_QUIZ_TIMES to FieldValue.arrayUnion(
                    averageQuizTime
                ),
                DatabaseCommon.UserCollection.FIELD_TOTAL_QUESTIONS_PLAYED to FieldValue.increment(
                    totalQuestionsPlayed
                ),
                DatabaseCommon.UserCollection.FIELD_CORRECT_ANSWERS to FieldValue.increment(
                    totalCorrectAnswers
                ),
            )

            val newUserMapWithDiamonds = getNewUserMapWithDiamonds(user, newXp)

            transition.update(userDoc, newUserUpdateMap + newUserMapWithDiamonds)
        }.await()
    }

    private fun getNewUserMapWithDiamonds(
        currentUser: UserEntity,
        newXp: Long
    ): Map<String, Any> = if (currentUser.toUser().isNewLevel(newXp)) {
        val newLevelDiamondsReward = remoteConfig.getLong("new_level_diamonds_reward")
        val newLevelMultiplierEnabled = remoteConfig.getBoolean("new_level_diamonds_multiplier_by_level")

        val newUserLevel = currentUser.toUser().getLevelAfter(newXp)

        val newDiamonds = if (newLevelMultiplierEnabled) {
            newLevelDiamondsReward * newUserLevel
        } else newLevelDiamondsReward

        coreLoggingAnalytics.logNewLevel(newUserLevel, newDiamonds.toInt())

        mapOf(DatabaseCommon.UserCollection.FIELD_DIAMONDS to FieldValue.increment(newDiamonds))
    } else emptyMap()

    override suspend fun updateLocalUserDiamonds(n: Int) {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        usersCol
            .document(localUid)
            .update(
                DatabaseCommon.UserCollection.FIELD_DIAMONDS,
                FieldValue.increment(n.toLong())
            ).await()
    }
}