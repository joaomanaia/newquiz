package com.infinitepower.newquiz.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsDataStoreManager