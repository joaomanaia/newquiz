package com.infinitepower.newquiz.core.database.di

import com.infinitepower.newquiz.core.database.AppDatabase
import com.infinitepower.newquiz.core.database.dao.ComparisonQuizDao
import com.infinitepower.newquiz.core.database.dao.DailyChallengeDao
import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.database.dao.MazeQuizDao
import com.infinitepower.newquiz.core.database.dao.SavedMultiChoiceQuestionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideSavedQuestionsDao(
        appDatabase: AppDatabase
    ): SavedMultiChoiceQuestionsDao = appDatabase.savedQuestionsDao()

    @Provides
    fun provideMazeQuizDao(
        appDatabase: AppDatabase
    ): MazeQuizDao = appDatabase.mazeQuizDao()

    @Provides
    fun provideDailyChallengeDao(
        appDatabase: AppDatabase
    ): DailyChallengeDao = appDatabase.dailyChallengeDao()

    @Provides
    fun provideComparisonQuizDao(
        appDatabase: AppDatabase
    ): ComparisonQuizDao = appDatabase.comparisonQuizDao()

    @Provides
    fun provideGameResultDao(
        appDatabase: AppDatabase
    ): GameResultDao = appDatabase.gameResultDao()
}