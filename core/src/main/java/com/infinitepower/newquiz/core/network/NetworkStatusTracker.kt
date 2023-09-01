package com.infinitepower.newquiz.core.network

import kotlinx.coroutines.flow.Flow

interface NetworkStatusTracker {
    val isOnline: Flow<Boolean>

    fun isCurrentlyConnected(): Boolean
}