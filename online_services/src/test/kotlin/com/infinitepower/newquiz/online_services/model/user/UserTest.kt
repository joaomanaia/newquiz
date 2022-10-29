package com.infinitepower.newquiz.online_services.model.user

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserTest {
    @Test
    @DisplayName("Should return the correct value when the user is level 1")
    fun getXPToNextLevelWhenUserIsLevel1() {
        val user = User(data = User.UserData(totalXp = 100))

        val requiredXP = user.getRequiredXP()

        assertThat(requiredXP).isEqualTo(300)
    }

    @Test
    @DisplayName("Should return the correct value when the user is level 2")
    fun getXPToNextLevelWhenUserIsLevel2() {
        val user = User(
            data = User.UserData(
                totalXp = 2361
            )
        )

        val requiredXP = user.getRequiredXP()

        assertThat(requiredXP).isEqualTo(139)
    }

    @Test
    @DisplayName("Should return 0 when the user has no xp")
    fun getLevelProgressWhenUserHasNoXpThenReturn0() {
        val user = User()

        val levelProgress = user.getLevelProgress()

        assertThat(levelProgress).isEqualTo(0f)
    }

    @Test
    @DisplayName("Should return 0.9444 when the user has enough xp to level up")
    fun getLevelProgressWhenUserHasEnoughXpToLevelUpThenReturn1() {
        val user = User(data = User.UserData(totalXp = 2361))

        val levelProgress = user.getLevelProgress()

        assertThat(levelProgress).isEqualTo(0.9444f)
    }

    @Test
    @DisplayName("Should return the correct next level xp when the user has 0 xp")
    fun getNextLevelXpWhenUserHas0XpThenReturnCorrectNextLevelXp() {
        val user = User()
        val nextLevelXp = user.getNextLevelXp()

        assertThat(nextLevelXp).isEqualTo(100)
    }

    @Test
    @DisplayName("Should return the correct next level xp when the user has 100 xp")
    fun getNextLevelXpWhenUserHas100XpThenReturnCorrectNextLevelXp() {
        val user = User(data = User.UserData(totalXp = 100))

        val nextLevelXp = user.getNextLevelXp()

        assertThat(nextLevelXp).isEqualTo(400)
    }

    @Test
    @DisplayName("Should return 0 when the totalxp is 0")
    fun getLevelWhenTotalXpIs0ThenReturn1() {
        val user = User(data = User.UserData(totalXp = 0))
        val level = user.level

        assertThat(level).isEqualTo(0)
    }

    @Test
    @DisplayName("Should return 1 when the totalxp is 100")
    fun getLevelWhenTotalXpIs100ThenReturn1() {
        val user = User(data = User.UserData(totalXp = 100))
        val level = user.level

        assertThat(level).isEqualTo(1)
    }

    @Test
    @DisplayName("Should return 8 when the totalxp is 8099")
    fun getLevelWhenTotalXpIs8099ThenReturn8() {
        val user = User(data = User.UserData(totalXp = 8099))
        val level = user.level

        assertThat(level).isEqualTo(8)
    }
}