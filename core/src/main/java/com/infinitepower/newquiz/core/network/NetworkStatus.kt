package com.infinitepower.newquiz.core.network

sealed class NetworkStatus {
    object Available : NetworkStatus()

    object Unavailable : NetworkStatus()

    object Unknown : NetworkStatus()

    fun isAvailable() = this is Available
}
