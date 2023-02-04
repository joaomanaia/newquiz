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
    }

    init {
        require(value >= Duration.ZERO) { "RemainingTime value must be greater than 0" }
    }

    /**
     * @return the remaining percentage of the time.
     * @param maxTime in milliseconds
     */
    fun getRemainingPercent(maxTime: Long): Float = value.inWholeMilliseconds.toFloat() / maxTime

    /**
     * @return the remaining time in minute:second format.
     */
    fun minuteSecondFormatted(): String {
        val minutes = value.inWholeMinutes
        val seconds = value.inWholeSeconds % 60
        return if (minutes == 0L) seconds.toString() else "$minutes:$seconds"
    }

    /**
     * Returns the elapsed seconds from the max time.
     * @param maxTime in milliseconds
     */
    fun getElapsedSeconds(maxTime: Long): Long =
        maxTime.milliseconds.inWholeSeconds - value.inWholeSeconds
}