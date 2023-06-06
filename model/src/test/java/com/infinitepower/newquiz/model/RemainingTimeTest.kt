package com.infinitepower.newquiz.model

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal class RemainingTimeTest {
    @Test
    fun `creates a new instance of RemainingTime from zero time`() {
        val remainingTime = RemainingTime.ZERO

        assertThat(remainingTime.value).isEqualTo(Duration.ZERO)
    }

    @Test
    fun `creates a new instance of RemainingTime from the given duration value`() {
        val duration = 5.milliseconds
        val remainingTime = RemainingTime(duration)

        assertThat(remainingTime.value).isEqualTo(duration)

        val duration2 = 12.seconds
        val remainingTime2 = RemainingTime(duration2)

        assertThat(remainingTime2.value).isEqualTo(duration2)
    }

    @Test
    fun `fromMilliseconds creates a new instance of RemainingTime from the given milliseconds value`() {
        val millis = 300000L
        val remainingTime = RemainingTime.fromMilliseconds(millis)

        assertThat(remainingTime.value).isEqualTo(millis.milliseconds)
    }

    @Test
    fun `fromMilliseconds throws an exception when the value is lower than 0`() {
        val negativeMillis = -1000L

        val exception = assertThrows<IllegalArgumentException> {
            RemainingTime.fromMilliseconds(negativeMillis)
        }

        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("RemainingTime value must be greater than or equal to 0")
    }

    @Test
    fun `getRemainingPercent returns the remaining percentage of the time`() {
        val duration = 10.seconds
        val maxTime = 50.seconds
        val remainingTime = RemainingTime(duration)

        assertThat(remainingTime.getRemainingPercent(maxTime)).isEqualTo(0.2)
    }

    @Test
    fun `minuteSecondFormatted returns the remaining time in minute second format`() {
        val duration = 5.minutes + 10.seconds
        val remainingTime = RemainingTime(duration)

        assertThat(remainingTime.toMinuteSecondFormatted()).isEqualTo("5:10")
    }

    @Test
    fun `getElapsedSeconds returns the elapsed seconds from the max time`() {
        val duration = 10.seconds
        val maxTime = 60.seconds
        val remainingTime = RemainingTime(duration)

        val result = remainingTime.getElapsedSeconds(maxTime)
        assertThat(result).isEqualTo(50)
    }
}