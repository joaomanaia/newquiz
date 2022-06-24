package com.infinitepower.newquiz.compose.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Location
import androidx.room.Room
import androidx.work.WorkManager
import com.google.android.gms.ads.AdRequest
import com.infinitepower.newquiz.compose.core.ad.interstitial.InterstitialAdCore
import com.infinitepower.newquiz.compose.core.ad.interstitial.InterstitialAdCoreImpl
import com.infinitepower.newquiz.compose.core.firebase.EmulatorSuite
import com.infinitepower.newquiz.compose.core.util.quiz.QuizXPUtil
import com.infinitepower.newquiz.compose.data.local.AppDatabase
import com.infinitepower.newquiz.compose.data.local.question.SavedQuestionDao
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.data.remote.newquizapi.NewQuizApi
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import com.infinitepower.newquiz.compose.data.repository.auth.user.AuthUserApiImpl
import com.infinitepower.newquiz.compose.data.repository.newquizapi.NewQuizApiImpl
import com.infinitepower.newquiz.compose.data.repository.saved_questions.SavedQuestionsRepositoryImpl
import com.infinitepower.newquiz.compose.data.repository.user.UserApiImpl
import com.infinitepower.newquiz.compose.domain.repository.saved_questions.SavedQuestionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.faker
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providerKtorClient(): HttpClient = HttpClient(OkHttp) {
        engine {
            threadsCount = 4
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
    fun provideUserApi(
        authUserApi: AuthUserApi,
        userXPUtil: QuizXPUtil
    ): UserApi = UserApiImpl(authUserApi, userXPUtil)

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
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideSavedQuestionDao(
        appDatabase: AppDatabase
    ): SavedQuestionDao = appDatabase.savedQuestionDao()

    @Provides
    @Singleton
    fun provideSavedQuestionsRepositoryImpl(
        savedQuestionDao: SavedQuestionDao
    ): SavedQuestionsRepository = SavedQuestionsRepositoryImpl(savedQuestionDao)

    @Provides
    @Singleton
    fun provideFaker(): Faker = faker {
        fakerConfig {
            locale = Locale.getDefault().country.lowercase()
        }
    }
}