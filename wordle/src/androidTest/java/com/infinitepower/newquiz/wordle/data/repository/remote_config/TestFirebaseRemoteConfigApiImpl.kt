package com.infinitepower.newquiz.wordle.data.repository.remote_config

import com.infinitepower.newquiz.model.config.RemoteConfigApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestFirebaseRemoteConfigApiImpl @Inject constructor() : RemoteConfigApi {
    private val remoteConfigMap = mutableMapOf<String, Any>()

    override fun getLong(key: String): Long = remoteConfigMap[key] as Long
}