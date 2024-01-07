package com.infinitepower.newquiz.core.testing.di

import com.infinitepower.newquiz.data.di.KtorModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [KtorModule::class]
)
object TestKtorModule {
    @Provides
    @Singleton
    fun providerKtorClient(): HttpClient = HttpClient(MockEngine)
}
