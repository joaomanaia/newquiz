package com.infinitepower.newquiz.core.testing.di

import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.analytics.LocalDebugAnalyticsHelper
import com.infinitepower.newquiz.core.analytics.NormalAnalyticsModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NormalAnalyticsModule::class]
)
abstract class TestAnalyticsModule {
    @Binds
    abstract fun bindAnalyticsHelper(impl: LocalDebugAnalyticsHelper): AnalyticsHelper
}