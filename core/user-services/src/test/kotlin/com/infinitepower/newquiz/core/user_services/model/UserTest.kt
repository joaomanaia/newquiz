package com.infinitepower.newquiz.core.user_services.model

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

/**
 * Tests for [User].
 */
internal class UserTest {
    private fun getTestUser(
        uid: String = "testUid",
        fullName: String = "testFullName",
        imageUrl: String = "testImageUrl",
        totalXp: ULong = 0u,
        diamonds: UInt = 0u,
    ) = User(
        uid = uid,
        fullName = fullName,
        imageUrl = imageUrl,
        totalXp = totalXp,
        diamonds = diamonds,
    )

    @CsvSource(
        "0, 0",
        "99, 0",
        "100, 1",
        "101, 1",
        "9999, 9",
        "10000, 10",
        "10001, 10",
        "1000000, 100",
        "1000001, 100",
    )
    @ParameterizedTest(name = "totalXp = {0} -> level = {1}")
    fun `test user level`(
        totalXp: Long,
        expectedLevel: Int,
    ) {
        val user = getTestUser(totalXp = totalXp.toULong())

        assertThat(user.level).isEqualTo(expectedLevel.toUInt())
    }

    @Test
    fun `test get next level xp`() {
        val user = getTestUser(totalXp = 0u)

        // For level 0, next level xp is 100
        assertThat(user.getNextLevelXp()).isEqualTo(100u)
    }

    @CsvSource(
        "0, 0.0",
        "50, 0.5",
        "99, 0.99",
        "100, 0.0", // New level 1
        "399, 0.99",
        "400, 0.0" // New level 2
    )
    @ParameterizedTest(name = "totalXp = {0} should have progress = {1}")
    fun `test get level progress`(
        totalXp: Long,
        expectedProgress: Float,
    ) {
        val user = getTestUser(totalXp = totalXp.toULong())

        assertThat(user.getLevelProgress()).isWithin(0.01f).of(expectedProgress)
    }

    @Test
    fun `test get required xp`() {
        val user = getTestUser(totalXp = 0u)

        // For level 0, next level xp is 100
        assertThat(user.getRequiredXP()).isEqualTo(100uL)

        val user2 = getTestUser(totalXp = 100u)

        // For level 1, next level xp is 400, so required xp is 300
        assertThat(user2.getRequiredXP()).isEqualTo(300uL)
    }

    @Test
    fun `test is new level`() {
        val user = getTestUser(totalXp = 500u) // Level 2

        assertThat(user.isNewLevel(100u)).isFalse()
        assertThat(user.isNewLevel(400u)).isTrue()
        assertThat(user.isNewLevel(500u)).isTrue()
    }

    @Test
    fun `test get level after`() {
        val user = getTestUser(totalXp = 500u) // Level 2

        assertThat(user.getLevelAfter(100u)).isEqualTo(2u) // No level up
        assertThat(user.getLevelAfter(400u)).isEqualTo(3u)
        assertThat(user.getLevelAfter(500u)).isEqualTo(3u)
    }
}
