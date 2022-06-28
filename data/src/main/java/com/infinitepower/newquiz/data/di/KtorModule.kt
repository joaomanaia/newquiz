package com.infinitepower.newquiz.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorModule {
    @Provides
    @Singleton
    fun providerKtorClient(): HttpClient = HttpClient(OkHttp) {
        engine {
            threadsCount = 4
        }
    }
}