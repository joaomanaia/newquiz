package com.infinitepower.newquiz.model.multi_choice_quiz

@JvmInline
value class RemainingTime private constructor(val value: Long) {
    companion object {
        val ZERO = RemainingTime(0L)

        fun fromValue(value: Long): RemainingTime = RemainingTime(value)

        const val MULTI_CHOICE_QUIZ_COUNTDOWN_IN_MILLIS = 30000L
    }

    init {
        require(value >= 0L) { "RemainingTime value must be greater than 0" }
    }

    fun getRemainingPercent(): Float = value.toFloat() / MULTI_CHOICE_QUIZ_COUNTDOWN_IN_MILLIS

    fun toMinuteSecond(): String {
        val minutes = (value / 1000) / 60
        val seconds = (value / 1000) % 60
        return if (minutes == 0L) seconds.toString() else "$minutes:$seconds"
    }
}