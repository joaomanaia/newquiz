package com.infinitepower.newquiz.online_services.core.network

sealed class NetworkStatus {
    object Available : NetworkStatus()

    object Unavailable : NetworkStatus()
}
