package com.infinitepower.newquiz.core.remote_config

import android.content.Context

class NoValueForRemoteConfigKeyException(key: String) : IllegalArgumentException("No value for key $key")

class LocalDefaultsRemoteConfig (
    private val context: Context
) : RemoteConfig {
    private val remoteConfigValues = mutableMapOf<String, String>()

    override fun initialize(fetchInterval: Long) {
        val parsedXmlValues = RemoteConfigXmlParser.parse(
            context = context,
            xmlResId = R.xml.remote_config_defaults
        )

        remoteConfigValues.clear()
        remoteConfigValues.putAll(parsedXmlValues)
    }

    override fun getString(key: String): String {
        return remoteConfigValues[key] ?: throw NoValueForRemoteConfigKeyException(key)
    }

    override fun getLong(key: String): Long {
        return remoteConfigValues[key]?.toLong() ?: throw NoValueForRemoteConfigKeyException(key)
    }

    override fun getBoolean(key: String): Boolean {
        return remoteConfigValues[key]?.toBoolean() ?: throw NoValueForRemoteConfigKeyException(key)
    }
}
