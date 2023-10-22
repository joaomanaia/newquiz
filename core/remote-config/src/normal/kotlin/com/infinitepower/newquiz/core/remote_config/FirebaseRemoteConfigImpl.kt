package com.infinitepower.newquiz.core.remote_config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class FirebaseRemoteConfigImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfig {
    override fun initialize(fetchInterval: Long) {
        val remoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = fetchInterval
        }
        firebaseRemoteConfig.setConfigSettingsAsync(remoteConfigSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        firebaseRemoteConfig.fetchAndActivate()
    }

    override fun getString(key: String): String = firebaseRemoteConfig.getString(key)

    override fun getLong(key: String): Long = firebaseRemoteConfig.getLong(key)

    override fun getBoolean(key: String): Boolean = firebaseRemoteConfig.getBoolean(key)
}