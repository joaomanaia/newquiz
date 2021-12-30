package com.infinitepower.newquiz.compose.di

import com.infinitepower.newquiz.compose.data.remote.opentdb.NewQuizApi
import com.infinitepower.newquiz.compose.data.repository.question.FakeQuestionApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Singleton
    fun providerKtorClient(): HttpClient = HttpClient(Android) {
        engine {
            threadsCount = 4
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
            accept(ContentType.Application.Json)
        }
    }

    @Provides
    @Singleton
    fun provideQuestionApi(): NewQuizApi = FakeQuestionApiImpl()
}