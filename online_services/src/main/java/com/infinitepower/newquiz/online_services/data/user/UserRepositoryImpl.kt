package com.infinitepower.newquiz.online_services.data.user

import com.google.firebase.firestore.FieldValue
import com.infinitepower.newquiz.core.common.database.DatabaseCommon
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import com.infinitepower.newquiz.online_services.model.user.User
import com.infinitepower.newquiz.online_services.model.user.UserNotLoggedInException
import com.infinitepower.newquiz.online_services.model.user.toUser
import com.infinitepower.newquiz.online_services.model.user.toUserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val authUserRepository: AuthUserRepository,
    private val userApi: UserApi,
    private val remoteConfig: RemoteConfig
) : UserRepository {
    override suspend fun getUserByUid(uid: String): User? {
        return userApi.getUserByUid(uid)?.toUser()
    }

    @Throws(UserNotLoggedInException::class)
    override suspend fun getLocalUser(): User? {
        val localUid = authUserRepository.uid ?: throw UserNotLoggedInException()

        return getUserByUid(localUid)
    }

    override suspend fun createUser(user: User) {
        // Check if the data is valid
        require(user.data.diamonds in 0..100) { "Diamonds must be between 0 and 100" }
        require(user.data.totalXp == 0uL) { "Total XP must be 0" }
        require(user.data.multiChoiceQuizData.totalQuestionsPlayed == 0uL) { "Total questions played must be 0" }
        require(user.data.multiChoiceQuizData.totalCorrectAnswers == 0uL) { "Total correct answers must be 0" }
        require(user.data.multiChoiceQuizData.lastQuizTimes.isEmpty()) { "Last quiz times must be empty" }
        require(user.data.wordleData.wordsPlayed == 0uL) { "Words played must be 0" }
        require(user.data.wordleData.wordsCorrect == 0uL) { "Words correct must be 0" }

        userApi.createUser(user.toUserEntity())
    }

    override suspend fun updateLocalUser(
        newXp: ULong,
        wordsPlayed: ULong,
        wordsCorrect: ULong
    ) {
        userApi.updateLocalUserTransaction { currentUser ->
            val updateData = mapOf(
                DatabaseCommon.UserCollection.FIELD_TOTAL_XP to FieldValue.increment(newXp.toLong()),
                DatabaseCommon.UserCollection.FIELD_WORDLE_WORDS_PLAYED to FieldValue.increment(wordsPlayed.toLong()),
                DatabaseCommon.UserCollection.FIELD_WORDLE_WORDS_CORRECT to FieldValue.increment(wordsCorrect.toLong()),
            )

            if (currentUser.toUser().isNewLevel(newXp)) {
                updateData + getNewUserLevelMapWithDiamonds(remoteConfig, currentUser.toUser(), newXp)
            } else {
                updateData
            }
        }
    }

    override suspend fun updateLocalUser(
        newXp: ULong,
        averageQuizTime: Double,
        totalQuestionsPlayed: ULong,
        totalCorrectAnswers: ULong
    ) {
        userApi.updateLocalUser(newXp, averageQuizTime, totalQuestionsPlayed, totalCorrectAnswers)
    }

    override suspend fun addLocalUserDiamonds(diamonds: Int) {
        // Max diamonds to update is 100
        require(diamonds <= 100) { "Diamonds to update must be less than or equal to 100" }

        userApi.updateLocalUser(
            mapOf(
                DatabaseCommon.UserCollection.FIELD_DIAMONDS to FieldValue.increment(diamonds.toLong())
            )
        )
    }
}

internal fun getNewUserLevelMapWithDiamonds(
    remoteConfig: RemoteConfig,
    currentUser: User,
    newXp: ULong
): Map<String, Any> {
    // Fetch the new level diamonds reward from remote config
    // TODO: Change this to a remote config value
    val newLevelDiamondsReward = 1uL

    // Calculate the new level and diamonds
    val newUserLevel = currentUser.getLevelAfter(newXp)
    val newDiamonds = newLevelDiamondsReward * newUserLevel

    return mapOf(
        DatabaseCommon.UserCollection.FIELD_DIAMONDS to FieldValue.increment(newDiamonds.toLong())
    )
}
