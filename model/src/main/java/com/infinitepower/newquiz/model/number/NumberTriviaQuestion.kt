package com.infinitepower.newquiz.model.number

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class NumberTriviaQuestion(
    val number: Int,
    val question: String
) : java.io.Serializable
