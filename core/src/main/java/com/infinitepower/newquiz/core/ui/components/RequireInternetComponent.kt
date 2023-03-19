package com.infinitepower.newquiz.core.ui.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.infinitepower.newquiz.core.ui.StatusWrapper


/**
 * This component will check if the user has internet connection,
 * and will make the content in [StatusWrapper], the content will be enabled if the user has internet connection.
 */
@Composable
fun RequireInternetComponent(
    content: @Composable (enabled: Boolean) -> Unit
) {
    val internetAvailable = rememberIsInternetAvailable()

    content(internetAvailable)
}

@Composable
fun rememberIsInternetAvailable(): Boolean {
    val context = LocalContext.current

    // Check if the user has internet connection
    return remember { context.isInternetAvailable() }
}

@Suppress("DEPRECATION")
private fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return actNw.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }
    } else {
        val type = connectivityManager.activeNetworkInfo?.type ?: return false

        return type == ConnectivityManager.TYPE_WIFI ||
            type == ConnectivityManager.TYPE_MOBILE ||
            type == ConnectivityManager.TYPE_ETHERNET
    }
}
