package com.infinitepower.newquiz.online_services.core

import kotlinx.coroutines.flow.Flow

interface OnlineServicesCore {
    suspend fun connectionAvailable(): Boolean

    fun connectionAvailableFlow(): Flow<Boolean>
}