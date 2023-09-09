package com.infinitepower.newquiz.core.database.di

import android.content.Context
import androidx.room.Room
import com.infinitepower.newquiz.core.database.AppDatabase
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
}