package com.infinitepower.newquiz.core.analytics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FossAnalyticsModule {
    @Binds
    abstract fun bindAnalyticsHelper(impl: LocalDebugAnalyticsHelper): AnalyticsHelper
}