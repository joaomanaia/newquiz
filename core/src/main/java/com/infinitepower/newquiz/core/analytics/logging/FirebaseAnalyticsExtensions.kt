package com.infinitepower.newquiz.core.analytics.logging

import androidx.annotation.Size
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder

fun ParametersBuilder.param(key: String, value: Int) = param(key, value.toLong())

fun ParametersBuilder.param(key: String, value: Boolean) = param(key, value.toString())

fun FirebaseAnalytics.setUserProperty(
    @Size(min = 1L, max = 24L) name: String,
    value: Boolean
) {
    setUserProperty(name, value.toString())
}