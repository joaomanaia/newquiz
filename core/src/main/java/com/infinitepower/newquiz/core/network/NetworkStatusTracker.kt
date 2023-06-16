package com.infinitepower.newquiz.core.network

import kotlinx.coroutines.flow.Flow

interface NetworkStatusTracker {
    val networkStatus: Flow<NetworkStatus>

    suspend fun connectionAvailable(): Boolean
}