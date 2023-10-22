package com.infinitepower.newquiz.core.remote_config.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.infinitepower.newquiz.core.remote_config.LocalDefaultsRemoteConfig
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
        Log.d(TAG, "Initializing local remote config")

        val remoteConfig = LocalDefaultsRemoteConfig(context = context)
        remoteConfig.initialize()

        Log.d(TAG, "Local remote config initialized successfully")

        return remoteConfig
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
