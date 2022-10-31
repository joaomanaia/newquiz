package com.infinitepower.newquiz.online_services.data.game.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.online_services.domain.game.xp.WordleXpRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WordleXpRepositoryImplTest {
    private lateinit var wordleXpRepository: WordleXpRepository

    @BeforeEach
    fun setup() {
        wordleXpRepository = WordleXpRepositoryImpl()
    }

    @Test
    @DisplayName("Should return a random number multiplied by 2.5f / 2 used")
    fun generateRandomXPWhenRowsUsedIs2ThenReturnARandomNumberMultipliedBy2_5fDividedByRowsUsed() {
        val randomXP = wordleXpRepository.generateRandomXP(2)

        assertThat(randomXP).isIn(12..25)
    }
}