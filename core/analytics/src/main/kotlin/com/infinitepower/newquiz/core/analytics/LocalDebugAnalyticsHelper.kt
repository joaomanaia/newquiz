package com.infinitepower.newquiz.core.analytics

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LocalAnalyticsHelper"

@Singleton
class LocalDebugAnalyticsHelper @Inject constructor() : AnalyticsHelper {
    override fun logEvent(vararg events: AnalyticsEvent) {
        Log.d(TAG, "Received events: ${events.joinToString()}")
    }

    override fun setUserProperty(userProperty: UserProperty) {
        Log.d(TAG, "Received user property: $userProperty")
    }

    override fun enableAll(enabled: Boolean) {
        Log.d(TAG, "enableAll: $enabled")
    }

    override fun setGeneralAnalyticsEnabled(enabled: Boolean) {
        Log.d(TAG, "enableGeneralAnalytics: $enabled")
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        Log.d(TAG, "enableCrashlytics: $enabled")
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        Log.d(TAG, "enablePerformanceMonitoring: $enabled")
    }
}
