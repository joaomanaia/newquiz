package com.infinitepower.newquiz.online_services.data

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.model.config.RemoteConfigApi
import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.model.user.User
import com.infinitepower.newquiz.online_services.model.user.toUserEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Tests for [UserRepositoryImpl]
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class UserRepositoryImplTest {
    private val authUserRepository = mockk<AuthUserRepository>()
    private val userApi = mockk<UserApi>()
    private val remoteConfigApi = mockk<RemoteConfigApi>()

    private val userRepository = UserRepositoryImpl(
        authUserRepository = authUserRepository,
        userApi = userApi,
        remoteConfigApi = remoteConfigApi
    )

    private val testUser = User(
        uid = "a",
        data = User.UserData(
            diamonds = 50u,
            totalXp = 0uL,
            multiChoiceQuizData = User.UserData.MultiChoiceQuizData(
                totalQuestionsPlayed = 0uL,
                totalCorrectAnswers = 0uL,
                lastQuizTimes = emptyList()
            ),
            wordleData = User.UserData.WordleData(
                wordsPlayed = 0uL,
                wordsCorrect = 0uL
            )
        )
    )

    @Test
    fun `test getUserByUid`() = runTest {
        coEvery { userApi.getUserByUid("a") } returns testUser.toUserEntity()

        val user = userRepository.getUserByUid("a")

        assertThat(user).isEqualTo(testUser)

        coVerify(exactly = 1) { userApi.getUserByUid("a") }
    }

    @Test
    fun `test getLocalUser`() = runTest {
        every { authUserRepository.uid } returns "a"
        coEvery { userApi.getUserByUid("a") } returns testUser.toUserEntity()

        val user = userRepository.getLocalUser()

        assertThat(user).isEqualTo(testUser)

        coVerify(exactly = 1) { userApi.getUserByUid("a") }
    }

    @Test
    fun `createUserDB should create user when all user data is valid`() = runTest {
        coEvery { userApi.createUser(any()) } returns Unit

        userRepository.createUser(testUser)

        assertThat(testUser.data.diamonds).isIn(0u..100u)
        assertThat(testUser.data.totalXp).isEqualTo(0uL)
        assertThat(testUser.data.multiChoiceQuizData.totalQuestionsPlayed).isEqualTo(0uL)
        assertThat(testUser.data.multiChoiceQuizData.totalCorrectAnswers).isEqualTo(0uL)
        assertThat(testUser.data.multiChoiceQuizData.lastQuizTimes).isEmpty()
        assertThat(testUser.data.wordleData.wordsPlayed).isEqualTo(0uL)
        assertThat(testUser.data.wordleData.wordsCorrect).isEqualTo(0uL)

        coVerify(exactly = 1) { userApi.createUser(any()) }
    }
}