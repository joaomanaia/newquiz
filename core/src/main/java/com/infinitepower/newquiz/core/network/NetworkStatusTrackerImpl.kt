package com.infinitepower.newquiz.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStatusTrackerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkStatusTracker {
    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()

        // Close the flow if connectivityManager is null
        if (connectivityManager == null) {
            trySend(false)
            close()
            return@callbackFlow
        }

        /**
         * The callback's methods are invoked on changes to *any* network matching the [NetworkRequest],
         * not just the active network. So we can simply track the presence (or absence) of such [Network].
         */
        val callback = object : ConnectivityManager.NetworkCallback() {
            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks += network
                trySend(true)
            }

            override fun onLost(network: Network) {
                networks -= network
                // If there are no networks, we're offline
                trySend(networks.isNotEmpty())
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        Log.d("NetworkStatusTracker", "Registering network callback")
        connectivityManager.registerNetworkCallback(request, callback)

        /**
         * Sends the latest connectivity status to the underlying channel.
         */
        trySend(connectivityManager.isCurrentlyConnected())

        /**
         * When the flow is closed, unregisters the callback.
         */
        awaitClose {
            Log.d("NetworkStatusTracker", "Unregistering network callback")
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.conflate()

    @Suppress("DEPRECATION")
    private fun ConnectivityManager.isCurrentlyConnected() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            activeNetwork
                ?.let(::getNetworkCapabilities)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        else -> activeNetworkInfo?.isConnected
    } ?: false

    override fun isCurrentlyConnected(): Boolean {
        val connectivityManager = context.getSystemService<ConnectivityManager>() ?: return false

        return connectivityManager.isCurrentlyConnected()
    }
}
