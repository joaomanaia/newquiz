package com.infinitepower.newquiz.core_test.initializer

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestWorkManagerInitializer : Initializer<WorkManager> {
    @Provides
    @Singleton
    override fun create(@ApplicationContext context: Context): WorkManager {
        if (!WorkManager.isInitialized()) {
            val entryPoint = EntryPointAccessors.fromApplication<WorkManagerInitializerEntryPoint>(context)

            val configuration = Configuration
                .Builder()
                .setWorkerFactory(entryPoint.hiltWorkerFactory())
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(SynchronousExecutor())
                .build()

            WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)
        }

        return WorkManager.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkManagerInitializerEntryPoint {
        fun hiltWorkerFactory(): HiltWorkerFactory
    }
}