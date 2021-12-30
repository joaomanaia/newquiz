package com.infinitepower.newquiz.compose.di

import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.data.remote.opentdb.NewQuizApi
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import com.infinitepower.newquiz.compose.data.repository.auth.user.AuthUserApiImpl
import com.infinitepower.newquiz.compose.data.repository.opentdb.NewQuizApiImpl
import com.infinitepower.newquiz.compose.data.repository.user.UserApiImpl
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
object AppModule {
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
    fun provideQuestionApi(client: HttpClient): NewQuizApi = NewQuizApiImpl(client)

    @Provides
    @Singleton
    fun provideAuthUserApi(): AuthUserApi = AuthUserApiImpl()

    @Provides
    @Singleton
    fun provideUserApi(): UserApi = UserApiImpl()
}