package com.infinitepower.newquiz.core.testing.di

import android.content.Context
import com.infinitepower.newquiz.core.remote_config.LocalDefaultsRemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.initializer.RemoteConfigInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteConfigInitializer::class]
)
object TestRemoteConfigModule {
    @Provides
    @Singleton
    fun provideRemoteConfig(
        @ApplicationContext context: Context
    ): RemoteConfig = LocalDefaultsRemoteConfig(context)
}
