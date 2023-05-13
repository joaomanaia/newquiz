package com.infinitepower.newquiz.model.config

interface RemoteConfigApi {
    fun getLong(key: String): Long
}