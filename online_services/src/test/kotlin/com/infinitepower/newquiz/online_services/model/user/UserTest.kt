package com.infinitepower.newquiz.online_services.model.user

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserTest {
    @Test
    @DisplayName("Should return the correct value when the user is level 1")
    fun getXPToNextLevelWhenUserIsLevel1() {
        val user = User(
            uid = "a",
            data = User.UserData(totalXp = 100u)
        )

        val requiredXP = user.getRequiredXP()

        assertThat(requiredXP).isEqualTo(300uL)
    }

    @Test
    @DisplayName("Should return the correct value when the user is level 2")
    fun getXPToNextLevelWhenUserIsLevel2() {
        val user = User(
            uid = "a",
            data = User.UserData(
                totalXp = 2361u
            )
        )

        val requiredXP = user.getRequiredXP()

        assertThat(requiredXP).isEqualTo(139uL)
    }

    @Test
    @DisplayName("Should return 0 when the user has no xp")
    fun getLevelProgressWhenUserHasNoXpThenReturn0() {
        val user = User( uid = "a")

        val levelProgress = user.getLevelProgress()

        assertThat(levelProgress).isEqualTo(0f)
    }

    @Test
    @DisplayName("Should return 0.9444 when the user has enough xp to level up")
    fun getLevelProgressWhenUserHasEnoughXpToLevelUpThenReturn1() {
        val user = User(
            uid = "a",
            data = User.UserData(totalXp = 2361u)
        )

        val levelProgress = user.getLevelProgress()

        assertThat(levelProgress).isEqualTo(0.9444f)
    }

    @Test
    @DisplayName("Should return the correct next level xp when the user has 0 xp")
    fun getNextLevelXpWhenUserHas0XpThenReturnCorrectNextLevelXp() {
        val user = User( uid = "a")
        val nextLevelXp = user.getNextLevelXp()

        assertThat(nextLevelXp).isEqualTo(100u)
    }

    @Test
    @DisplayName("Should return the correct next level xp when the user has 100 xp")
    fun getNextLevelXpWhenUserHas100XpThenReturnCorrectNextLevelXp() {
        val user = User(
            uid = "a",
            data = User.UserData(totalXp = 100u)
        )

        val nextLevelXp = user.getNextLevelXp()

        assertThat(nextLevelXp).isEqualTo(400u)
    }

    @Test
    @DisplayName("Should return 0 when the totalxp is 0")
    fun getLevelWhenTotalXpIs0ThenReturn1() {
        val user = User(
            uid = "a",
            data = User.UserData(totalXp = 0u)
        )
        val level = user.level

        assertThat(level).isEqualTo(0u)
    }

    @Test
    @DisplayName("Should return 1 when the totalxp is 100")
    fun getLevelWhenTotalXpIs100ThenReturn1() {
        val user = User(
            uid = "a",
            data = User.UserData(totalXp = 100u)
        )
        val level = user.level

        assertThat(level).isEqualTo(1u)
    }

    @Test
    @DisplayName("Should return 8 when the totalxp is 8099")
    fun getLevelWhenTotalXpIs8099ThenReturn8() {
        val user = User(
            uid = "a",
            data = User.UserData(totalXp = 8099u)
        )
        val level = user.level

        assertThat(level).isEqualTo(8u)
    }
}