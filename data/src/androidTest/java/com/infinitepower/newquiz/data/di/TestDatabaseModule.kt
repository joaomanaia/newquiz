package com.infinitepower.newquiz.data.di

import android.content.Context
import androidx.room.Room
import com.infinitepower.newquiz.data.database.AppDatabase
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeDao
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizDao
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsDao
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase = Room.inMemoryDatabaseBuilder(
        applicationContext,
        AppDatabase::class.java
    ).build()

    @Provides
    @Singleton
    fun provideSavedQuestionsDao(
        appDatabase: AppDatabase
    ): SavedMultiChoiceQuestionsDao = appDatabase.savedQuestionsDao()

    @Provides
    @Singleton
    fun provideDailyWordleDao(
        appDatabase: AppDatabase
    ): DailyWordleDao = appDatabase.dailyWordleDao()

    @Provides
    @Singleton
    fun provideMazeQuizDao(
        appDatabase: AppDatabase
    ): MazeQuizDao = appDatabase.mazeQuizDao()

    @Provides
    @Singleton
    fun provideDailyChallengeDao(
        appDatabase: AppDatabase
    ): DailyChallengeDao = appDatabase.dailyChallengeDao()
}