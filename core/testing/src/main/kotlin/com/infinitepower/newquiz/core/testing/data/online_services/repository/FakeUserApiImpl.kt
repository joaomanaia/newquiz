package com.infinitepower.newquiz.core.testing.data.online_services.repository

import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import com.infinitepower.newquiz.online_services.model.user.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUserApiImpl @Inject constructor(
    private val authUserRepository: AuthUserRepository
) : UserApi {
    private val users = mutableListOf<UserEntity>()

    override suspend fun getUserByUid(uid: String): UserEntity? {
        return users.find { it.uid == uid }
    }

    override suspend fun createUser(user: UserEntity) {
        users.add(user)
    }

    override suspend fun updateLocalUser(newData: Map<String, Any>) {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        val user = users.find {
            it.uid == localUid
        } ?: throw NullPointerException("User is not logged in")

        users.remove(user)

        users.add(
            user.copy(
                data = UserEntity.UserData(
                    totalXp = newData["totalXp"] as Long?,
                    diamonds = newData["diamonds"] as Int?,
                    multiChoiceQuizData = UserEntity.UserData.MultiChoiceQuizData(
                        totalQuestionsPlayed = newData["totalQuestionsPlayed"] as Long?,
                        totalCorrectAnswers = newData["totalCorrectAnswers"] as Long?,
                        lastQuizTimes = newData["lastQuizTimes"] as List<Double>?
                    ),
                    wordleData = UserEntity.UserData.WordleData(
                        wordsPlayed = newData["wordsPlayed"] as Long?,
                        wordsCorrect = newData["wordsCorrect"] as Long?,
                    )
                )
            )
        )
    }

    override suspend fun updateLocalUserTransaction(newData: (currentUser: UserEntity) -> Map<String, Any>) {
val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        val user = users.find {
            it.uid == localUid
        } ?: throw NullPointerException("User is not logged in")

        users.remove(user)

        users.add(
            user.copy(
                data = UserEntity.UserData(
                    totalXp = newData(user)["totalXp"] as Long?,
                    diamonds = newData(user)["diamonds"] as Int?,
                    multiChoiceQuizData = UserEntity.UserData.MultiChoiceQuizData(
                        totalQuestionsPlayed = newData(user)["totalQuestionsPlayed"] as Long?,
                        totalCorrectAnswers = newData(user)["totalCorrectAnswers"] as Long?,
                        lastQuizTimes = newData(user)["lastQuizTimes"] as List<Double>?
                    ),
                    wordleData = UserEntity.UserData.WordleData(
                        wordsPlayed = newData(user)["wordsPlayed"] as Long?,
                        wordsCorrect = newData(user)["wordsCorrect"] as Long?,
                    )
                )
            )
        )
    }

    override suspend fun updateLocalUser(
        newXp: ULong,
        averageQuizTime: Double,
        totalQuestionsPlayed: ULong,
        totalCorrectAnswers: ULong
    ) {
        val localUid = authUserRepository.uid ?: throw NullPointerException("User is not logged in")

        val user = users.find {
            it.uid == localUid
        } ?: throw NullPointerException("User is not logged in")

        users.remove(user)

        users.add(
            user.copy(
                data = UserEntity.UserData(
                    totalXp = newXp.toLong(),
                    multiChoiceQuizData = UserEntity.UserData.MultiChoiceQuizData(
                        totalQuestionsPlayed = totalQuestionsPlayed.toLong(),
                        totalCorrectAnswers = totalCorrectAnswers.toLong(),
                        lastQuizTimes = listOf(averageQuizTime)
                    )
                )
            )
        )
    }
}