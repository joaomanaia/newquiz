package com.infinitepower.newquiz.initializer.firebase

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.infinitepower.newquiz.BuildConfig
import com.infinitepower.newquiz.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseRemoteConfigInitializer : Initializer<FirebaseRemoteConfig> {
    @Provides
    @Singleton
    override fun create(@ApplicationContext context: Context): FirebaseRemoteConfig {
        val firebaseApp = Firebase.app
        val remoteConfig = FirebaseRemoteConfig.getInstance(firebaseApp)

        val remoteConfigMinFetchInterval = if (BuildConfig.DEBUG) 0L else 3600L
        val remoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = remoteConfigMinFetchInterval
        }
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()

        return remoteConfig
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        CoreFirebaseInitializer::class.java
    )
}