package com.infinitepower.newquiz.online_services.di

import com.infinitepower.newquiz.online_services.core.OnlineServicesCore
import com.infinitepower.newquiz.online_services.core.OnlineServicesCoreImpl
import com.infinitepower.newquiz.online_services.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.online_services.core.network.NetworkStatusTrackerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class OnlineServicesModule {
    @Binds
    abstract fun bindNetworkStatusTracker(
        networkStatusTrackerImpl: NetworkStatusTrackerImpl
    ): NetworkStatusTracker

    @Binds
    abstract fun bindOnlineServicesCore(
        onlineServicesCoreImpl: OnlineServicesCoreImpl
    ): OnlineServicesCore
}