package com.infinitepower.newquiz.compose.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.google.android.gms.ads.AdRequest
import com.infinitepower.newquiz.compose.core.firebase.EmulatorSuite
import com.infinitepower.newquiz.compose.data.local.AppDatabase
import com.infinitepower.newquiz.compose.data.local.question.SavedQuestionDao
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.data.remote.newquizapi.NewQuizApi
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import com.infinitepower.newquiz.compose.data.repository.auth.user.FakeAuthUserApiImpl
import com.infinitepower.newquiz.compose.data.repository.question.FakeQuestionApiImpl
import com.infinitepower.newquiz.compose.data.repository.user.FakeUserApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideAuthUserApi(): AuthUserApi = FakeAuthUserApiImpl()

    @Provides
    @Singleton
    fun provideUserApi(): UserApi = FakeUserApiImpl()

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext appContext: Context
    ): WorkManager = WorkManager.getInstance(appContext)

    @Provides
    @Singleton
    fun provideAdRequest(): AdRequest = AdRequest.Builder().build()

    @Provides
    @Singleton
    fun provideEmulatorSuite(): EmulatorSuite = EmulatorSuite()

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideSavedQuestionDao(
        appDatabase: AppDatabase
    ): SavedQuestionDao = appDatabase.savedQuestionDao()
}