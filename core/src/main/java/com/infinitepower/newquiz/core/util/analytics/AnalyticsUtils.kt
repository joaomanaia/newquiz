package com.infinitepower.newquiz.core.util.analytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance

object AnalyticsUtils {
    fun enableAll(enabled: Boolean) {
        enableGeneralAnalytics(enabled)
        enableCrashlytics(enabled)
        enablePerformanceMonitoring(enabled)
    }

    fun enableGeneralAnalytics(enabled: Boolean) {
        Firebase.analytics.setAnalyticsCollectionEnabled(enabled)
    }

    fun enableCrashlytics(enabled: Boolean) {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(enabled)
    }

    fun enablePerformanceMonitoring(enabled: Boolean) {
        Firebase.performance.isPerformanceCollectionEnabled = enabled
    }
}
