package com.infinitepower.newquiz.core.remote_config

/**
 * A [RemoteConfig] implementation that uses a map of remote config values.
 * Only used for testing.
 */
internal class LocalRemoteConfig(
    private val remoteConfigValues: Map<String, String>
) : RemoteConfig {
    override fun initialize(fetchInterval: Long) {
        // Do nothing
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
