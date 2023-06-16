package com.infinitepower.newquiz.core.di

import com.infinitepower.newquiz.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.core.network.NetworkStatusTrackerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkStatusModule {
    @Binds
    abstract fun bindNetworkStatusTracker(
        networkStatusTrackerImpl: NetworkStatusTrackerImpl
    ): NetworkStatusTracker
}