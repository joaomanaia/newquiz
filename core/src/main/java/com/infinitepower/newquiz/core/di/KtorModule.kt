package com.infinitepower.newquiz.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorModule {
    @Provides
    @Singleton
    fun providerKtorClient(): HttpClient = HttpClient(OkHttp)
}
