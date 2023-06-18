package com.infinitepower.newquiz.core.network

import kotlinx.coroutines.flow.StateFlow

interface NetworkStatusTracker {
    val networkStatus: StateFlow<NetworkStatus>

    suspend fun connectionAvailable(): Boolean
}