package com.infinitepower.newquiz.compose.util

import com.google.android.gms.common.util.Base64Utils
import com.infinitepower.newquiz.compose.data.local.question.Question

private fun decodeToString(coded: String) = String(Base64Utils.decode(coded))

fun Question.decodeBase64Question() = Question(
    id = id,
    description = decodeToString(description),
    imageUrl = imageUrl?.let { decodeToString(it) },
    options = options.map { option -> decodeToString(option) },
    lang = decodeToString(lang),
    category = decodeToString(category),
    correctAns = correctAns,
    type = decodeToString(type),
    difficulty = decodeToString(difficulty)
)