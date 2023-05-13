package com.infinitepower.newquiz.data.repository.remote_config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.infinitepower.newquiz.model.config.RemoteConfigApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigApiImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigApi {
    override fun getLong(key: String): Long = firebaseRemoteConfig.getLong(key)
}