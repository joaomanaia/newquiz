package com.infinitepower.newquiz.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.infinitepower.newquiz.core.datastore.common.recentCategoriesDataStore
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
object RecentCategoriesDatastoreModule {
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
    ): DataStoreManager = PreferencesDatastoreManager(dataStore)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RecentCategoriesDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RecentCategoriesDataStoreManager
