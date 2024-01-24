package com.infinitepower.newquiz.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@JvmInline
value class RemainingTime constructor(val value: Duration) {
    companion object {
        val ZERO = RemainingTime(Duration.ZERO)

        /**
         * Creates a new instance of [RemainingTime] from the given milliseconds value.
         */
        fun fromMilliseconds(millis: Long): RemainingTime {
            require(millis >= 0L) {
                "RemainingTime value must be greater than or equal to 0"
            }
            return RemainingTime(millis.milliseconds)
        }

        private const val SECONDS_IN_MINUTE = 60
    }

    init {
        require(value >= Duration.ZERO) { "RemainingTime value must be greater than 0" }
    }

    fun isZero(): Boolean = value == Duration.ZERO

    /**
     * @return the remaining percentage of the time.
     * @param maxTime in milliseconds
     */
    fun getRemainingPercent(maxTime: Duration): Double = value / maxTime

    /**
     * @return the remaining time in minute:second format.
     */
    fun toMinuteSecondFormatted(): String {
        val minutes = value.inWholeMinutes
        val seconds = value.inWholeSeconds % SECONDS_IN_MINUTE
        return if (minutes == 0L) seconds.toString() else "$minutes:$seconds"
    }

    /**
     * Returns the elapsed seconds from the max time.
     * @param maxTime in milliseconds
     */
    fun getElapsedSeconds(maxTime: Duration): Long = maxTime.inWholeSeconds - value.inWholeSeconds
}
