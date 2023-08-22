package com.infinitepower.newquiz.core.analytics

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LocalAnalyticsHelper"

@Singleton
class LocalDebugAnalyticsHelper @Inject constructor() : AnalyticsHelper {
    init {
        Log.d(TAG, "Local Initialized")
    }

    override fun logEvent(vararg events: AnalyticsEvent) {
        Log.d(TAG, "Received events: ${events.joinToString()}")
    }

    override fun setUserProperty(userProperty: UserProperty) {
        Log.d(TAG, "Received user property: $userProperty")
    }
}
