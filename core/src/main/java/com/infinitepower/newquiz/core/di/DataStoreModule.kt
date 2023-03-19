package com.infinitepower.newquiz.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.infinitepower.newquiz.core.common.dataStore.comparisonQuizDataStore
import com.infinitepower.newquiz.core.common.dataStore.multiChoiceCategoriesDataStore
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

    // Multi choice question data store

    @Provides
    @Singleton
    @MultiChoiceQuestionDataStore
    fun provideMultiChoiceQuestionDatastore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.multiChoiceCategoriesDataStore

    @Provides
    @Singleton
    @MultiChoiceQuestionDataStoreManager
    fun provideMultiChoiceQuestionStoreManager(
        @MultiChoiceQuestionDataStore dataStore: DataStore<Preferences>
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
}

// Settings data store
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsDataStoreManager

// Multi choice question data store

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MultiChoiceQuestionDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MultiChoiceQuestionDataStoreManager

// Comparison quiz data store

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ComparisonQuizDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ComparisonQuizDataStoreManager