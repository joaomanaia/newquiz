package com.infinitepower.newquiz.core.testing.data.repository.remote_config

import com.infinitepower.newquiz.model.config.RemoteConfigApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRemoteConfigApiImpl @Inject constructor() : RemoteConfigApi {
    private val remoteConfigMap = mutableMapOf<String, Any>()

    override fun getLong(key: String): Long = remoteConfigMap[key] as Long

    override fun getBoolean(key: String): Boolean = remoteConfigMap[key] as Boolean

    override fun getString(key: String): String = remoteConfigMap[key] as String
}