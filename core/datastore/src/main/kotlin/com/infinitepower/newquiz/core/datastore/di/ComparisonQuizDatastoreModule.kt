package com.infinitepower.newquiz.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.infinitepower.newquiz.core.datastore.common.comparisonQuizDataStore
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.PreferencesDatastoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ComparisonQuizDatastoreModule {
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
    ): DataStoreManager = PreferencesDatastoreManager(dataStore)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ComparisonQuizDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ComparisonQuizDataStoreManager
