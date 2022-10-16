package com.infinitepower.newquiz.online_services.core

import kotlinx.coroutines.flow.Flow

interface OnlineServicesCore {
    fun connectionAvailable(): Flow<Boolean>
}