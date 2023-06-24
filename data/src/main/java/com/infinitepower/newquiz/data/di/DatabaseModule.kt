package com.infinitepower.newquiz.data.di

import android.content.Context
import androidx.room.Room
import com.infinitepower.newquiz.data.database.AppDatabase
import com.infinitepower.newquiz.data.local.daily_challenge.DailyChallengeDao
import com.infinitepower.newquiz.data.local.maze.MazeQuizDao
import com.infinitepower.newquiz.data.local.multi_choice_quiz.SavedMultiChoiceQuestionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideSavedQuestionsDao(
        appDatabase: AppDatabase
    ): SavedMultiChoiceQuestionsDao = appDatabase.savedQuestionsDao()

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