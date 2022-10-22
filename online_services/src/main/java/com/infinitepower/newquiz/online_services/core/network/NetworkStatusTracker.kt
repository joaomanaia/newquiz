package com.infinitepower.newquiz.online_services.core.network

import kotlinx.coroutines.flow.Flow

interface NetworkStatusTracker {
    val netWorkStatus: Flow<NetworkStatus>
}