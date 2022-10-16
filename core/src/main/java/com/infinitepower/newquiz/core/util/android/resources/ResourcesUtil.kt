package com.infinitepower.newquiz.core.util.android.resources

import android.content.res.Resources
import androidx.annotation.RawRes
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

inline fun <reified T> Resources.readRawJson(@RawRes rawResId: Int): T {
    val resStr = openRawResource(rawResId)
        .readBytes()
        .decodeToString()

    return Json.decodeFromString(resStr)
}