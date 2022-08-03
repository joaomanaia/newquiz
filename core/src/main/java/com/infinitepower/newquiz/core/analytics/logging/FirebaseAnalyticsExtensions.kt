package com.infinitepower.newquiz.core.analytics.logging

import com.google.firebase.analytics.ktx.ParametersBuilder

fun ParametersBuilder.param(key: String, value: Int) = param(key, value.toLong())

fun ParametersBuilder.param(key: String, value: Boolean) = param(key, value.toString())