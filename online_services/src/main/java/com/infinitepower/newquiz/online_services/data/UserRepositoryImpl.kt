package com.infinitepower.newquiz.online_services.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.core.common.database.DatabaseCommon.UserCollection
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.online_services.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val authUserRepository: AuthUserRepository
) : UserRepository {
    private val firestore = Firebase.firestore

    private val usersCol by lazy {
        firestore.collection(UserCollection.NAME)
    }

    override suspend fun getUserByUid(uid: String): User? {
        val userDoc = usersCol.document(uid).get().await()
        return userDoc.toObject()
    }

    override suspend fun getLocalUser(): User? {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")
        return getUserByUid(localUid)
    }

    override suspend fun createUserDB(user: User) {
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
            val user = transition.get(userDoc).toObject<User>() ?: throw FirebaseFirestoreException(
                "User does not exist",
                FirebaseFirestoreException.Code.ABORTED
            )

            val newUserUpdateMap = mapOf(
                UserCollection.FIELD_TOTAL_XP to FieldValue.increment(newXp),
                UserCollection.FIELD_WORDLE_WORDS_PLAYED to FieldValue.increment(wordsPlayed),
                UserCollection.FIELD_WORDLE_WORDS_CORRECT to FieldValue.increment(wordsCorrect),
            )

            val newUserMapWithDiamonds = if (checkNewLevel(user, newXp)) {
                mapOf(UserCollection.FIELD_DIAMONDS to FieldValue.increment(10))
            } else emptyMap()

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
            val user = transition.get(userDoc).toObject<User>() ?: throw FirebaseFirestoreException(
                "User does not exist",
                FirebaseFirestoreException.Code.ABORTED
            )

            val lastQuizTimes = user.data?.multiChoiceQuizData?.lastQuizTimes.orEmpty()

            if (lastQuizTimes.size >= 5) {
                transition.update(
                    userDoc,
                    UserCollection.FIELD_LAST_QUIZ_TIMES,
                    FieldValue.arrayRemove(lastQuizTimes.first())
                )
            }

            val newUserUpdateMap = mapOf(
                UserCollection.FIELD_TOTAL_XP to FieldValue.increment(newXp),
                UserCollection.FIELD_LAST_QUIZ_TIMES to FieldValue.arrayUnion(
                    averageQuizTime
                ),
                UserCollection.FIELD_TOTAL_QUESTIONS_PLAYED to FieldValue.increment(
                    totalQuestionsPlayed
                ),
                UserCollection.FIELD_CORRECT_ANSWERS to FieldValue.increment(
                    totalCorrectAnswers
                ),
            )

            val newUserMapWithDiamonds = if (checkNewLevel(user, newXp)) {
                mapOf(UserCollection.FIELD_DIAMONDS to FieldValue.increment(10))
            } else emptyMap()

            transition.update(userDoc, newUserUpdateMap + newUserMapWithDiamonds)
        }.await()
    }

    private fun checkNewLevel(
        user: User,
        newXp: Long
    ): Boolean {
        val newTotalXP = user.totalXp + newXp
        val newUser = user.copy(data = User.UserData(totalXp = newTotalXP))

        return newUser.level > user.level
    }

    override suspend fun updateLocalUserDiamonds(n: Int) {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        usersCol
            .document(localUid)
            .update(
                UserCollection.FIELD_DIAMONDS,
                FieldValue.increment(n.toLong())
            ).await()
    }
}