package com.infinitepower.newquiz.model.comparison_quiz

import android.net.Uri
import androidx.annotation.Keep

@Keep
data class ComparisonQuizItem(
    val title: String,
    val value: Int,
    val imgUri: Uri
)
