package com.infinitepower.newquiz.data

import com.infinitepower.newquiz.model.config.RemoteConfigApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestFirebaseRemoteConfigApiImpl @Inject constructor() : RemoteConfigApi {
    private val remoteConfigMap = mutableMapOf<String, Any>()

    override fun getLong(key: String): Long = remoteConfigMap[key] as Long

    override fun getBoolean(key: String): Boolean = remoteConfigMap[key] as Boolean

    override fun getString(key: String): String = remoteConfigMap[key] as String
}