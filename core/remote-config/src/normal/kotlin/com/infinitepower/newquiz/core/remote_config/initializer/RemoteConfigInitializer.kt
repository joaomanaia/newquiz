package com.infinitepower.newquiz.core.remote_config.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.infinitepower.newquiz.core.initializer.CoreFirebaseInitializer
import com.infinitepower.newquiz.core.remote_config.FirebaseRemoteConfigImpl
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteConfigInitializer : Initializer<RemoteConfig> {
    private companion object {
        private const val TAG = "RemoteConfigInitializer"
    }

    @Provides
    @Singleton
    override fun create(@ApplicationContext context: Context): RemoteConfig {
        val firebaseApp = Firebase.app
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance(firebaseApp)

        Log.d(TAG, "Initializing Firebase Remote Config...")

        val remoteConfig = FirebaseRemoteConfigImpl(firebaseRemoteConfig = firebaseRemoteConfig)
        remoteConfig.initialize()

        Log.d(TAG, "Firebase Remote Config initialized successfully")

        return remoteConfig
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(CoreFirebaseInitializer::class.java)
    }
}