package com.infinitepower.newquiz.online_services.data

import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.online_services.model.user.User
import com.infinitepower.newquiz.online_services.model.user.toUser
import com.infinitepower.newquiz.online_services.model.user.toUserEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.Throws

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val authUserRepository: AuthUserRepository,
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getUserByUid(uid: String): User? {
        return userApi.getUserByUid(uid)?.toUser()
    }

    @Throws(NullPointerException::class)
    override suspend fun getLocalUser(): User? {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")
        return getUserByUid(localUid)
    }

    override suspend fun createUserDB(user: User) {
        userApi.createUserDB(user.toUserEntity())
    }

    override suspend fun updateLocalUserNewXPWordle(
        newXp: Long,
        wordsPlayed: Long,
        wordsCorrect: Long
    ) {
        userApi.updateLocalUserNewXPWordle(newXp, wordsPlayed, wordsCorrect)
    }

    override suspend fun updateLocalUserNewXP(
        newXp: Long,
        averageQuizTime: Double,
        totalQuestionsPlayed: Long,
        totalCorrectAnswers: Long
    ) {
        userApi.updateLocalUserNewXP(newXp, averageQuizTime, totalQuestionsPlayed, totalCorrectAnswers)
    }

    override suspend fun updateLocalUserDiamonds(n: Int) {
        // Max diamonds to update is 1000
        if (n > 1000) throw RuntimeException()

        userApi.updateLocalUserDiamonds(n)
    }
}