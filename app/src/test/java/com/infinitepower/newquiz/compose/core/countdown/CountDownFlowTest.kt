package com.infinitepower.newquiz.compose.core.countdown

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CountDownFlowTest {
    private lateinit var countDown: CountDownFlow

    @BeforeEach
    fun setup() {
        countDown = CountDownFlow(30000, 250)
    }

    @Test
    fun `countDownFlow, properly counts down from 30 to 0`() = runTest {
        var n = 30000
        countDown.start()
        countDown.countDownFlow.onEach {
            assertThat(it).isEqualTo(n)
            n -= 250
        }
    }
}