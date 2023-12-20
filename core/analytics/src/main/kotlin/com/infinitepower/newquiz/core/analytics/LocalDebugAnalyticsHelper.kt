package com.infinitepower.newquiz.core.analytics

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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

    override fun setGeneralAnalyticsEnabled(enabled: Boolean) {
        Log.d(TAG, "enableGeneralAnalytics: $enabled")
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        Log.d(TAG, "enableCrashlytics: $enabled")
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        Log.d(TAG, "enablePerformanceMonitoring: $enabled")
    }

    // Because this is a local debug analytics helper, we don't want to show the dialog to the user
    override val showDataAnalyticsConsentDialog: Flow<Boolean> = flowOf(false)

    override suspend fun updateDataConsent(agreed: Boolean) {
        Log.d(TAG, "updateDataConsent: $agreed")
    }
}
