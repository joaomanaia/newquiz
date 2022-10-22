package com.infinitepower.newquiz.online_services.core

import com.infinitepower.newquiz.online_services.core.network.NetworkStatus
import com.infinitepower.newquiz.online_services.core.network.NetworkStatusTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnlineServicesCoreImpl @Inject constructor(
    private val networkStatusTracker: NetworkStatusTracker
) : OnlineServicesCore {
    override fun connectionAvailable(): Flow<Boolean> = networkStatusTracker
        .netWorkStatus
        .map { it is NetworkStatus.Available }
}