package com.infinitepower.newquiz.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.infinitepower.newquiz.core.datastore.common.localUserDataStore
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
object LocalUserDataStoreModule {
    @Provides
    @Singleton
    @LocalUserDataStore
    fun provideLocalUserDatastore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.localUserDataStore

    @Provides
    @Singleton
    @LocalUserDataStoreManager
    fun provideLocalUserDataStoreManager(
        @LocalUserDataStore dataStore: DataStore<Preferences>
    ): DataStoreManager = PreferencesDatastoreManager(dataStore)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalUserDataStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalUserDataStoreManager