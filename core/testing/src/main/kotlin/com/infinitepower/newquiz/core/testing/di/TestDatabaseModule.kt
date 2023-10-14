package com.infinitepower.newquiz.core.testing.di

import android.content.Context
import androidx.room.Room
import com.infinitepower.newquiz.core.database.AppDatabase
import com.infinitepower.newquiz.core.database.di.DatabaseModule
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
}