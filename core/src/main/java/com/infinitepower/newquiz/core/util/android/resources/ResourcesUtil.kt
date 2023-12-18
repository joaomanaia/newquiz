package com.infinitepower.newquiz.core.util.android.resources

import android.content.res.Resources
import androidx.annotation.RawRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

suspend inline fun <reified T> Resources.readRawJson(@RawRes rawResId: Int): T {
    return withContext(Dispatchers.IO) {
        val resStr = openRawResource(rawResId).use {
            it.readBytes().decodeToString()
        }

        Json.decodeFromString(resStr)
    }
}