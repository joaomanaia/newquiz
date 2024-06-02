package com.infinitepower.newquiz.model

data class TimestampWithValue <T> (
    val timestamp: Long,
    val value: T
)
