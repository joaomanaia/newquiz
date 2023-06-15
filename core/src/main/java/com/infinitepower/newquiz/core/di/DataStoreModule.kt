package com.infinitepower.newquiz.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.infinitepower.newquiz.core.common.dataStore.comparisonQuizDataStore
import com.infinitepower.newquiz.core.common.dataStore.recentCategoriesDataStore
import com.infinitepower.newquiz.core.common.dataStore.settingsDataStore
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    // Settings data store
    @Provides
    @Singleton
    @SettingsDataStore
    fun provideSettingsDatastore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.settingsDataStore

    @Provides
    @Singleton
    @SettingsDataStoreManager
    fun provideDataStoreManager(
        @SettingsDataStore dataStore: DataStore<Preferences>
    ): DataStoreManager = DataStoreManagerImpl(dataStore)

    // Comparison quiz data store

    @Provides
    @Singleton
    @ComparisonQuizDataStore
    fun provideComparisonQuizDatastore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.comparisonQuizDataStore

    @Provides
    @Singleton
    @ComparisonQuizDataStoreManager
    fun provideComparisonQuizStoreManager(
        @ComparisonQuizDataStore dataStore: DataStore<Preferences>
    ): DataStoreManager = DataStoreManagerImpl(dataStore)

    // Recent categories data store

    @Provides
    @Singleton
    @RecentCategoriesDataStore
    fun provideRecentCategoriesDatastore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.recentCategoriesDataStore

    @Provides
    @Singleton
    @RecentCategoriesDataStoreManager
    fun provideRecentCategoriesStoreManager(
        @RecentCategoriesDataStore dataStore: DataStore<Preferences>
    ): DataStoreManager = DataStoreManagerImpl(dataStore)
}

// Settings data store
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsDataStoreManager

// Comparison quiz data store

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ComparisonQuizDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ComparisonQuizDataStoreManager

// Recent categories data store

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RecentCategoriesDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RecentCategoriesDataStoreManager
