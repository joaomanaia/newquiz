package com.infinitepower.newquiz.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebasePerformance: FirebasePerformance
) : AnalyticsHelper {
    private fun ParametersBuilder.param(
        analyticsParam: AnalyticsEvent.Param<*>
    ) {
        when (analyticsParam.value) {
            null -> {}
            is String -> param(analyticsParam.key, analyticsParam.value)
            is Long -> param(analyticsParam.key, analyticsParam.value)
            is Double -> param(analyticsParam.key, analyticsParam.value)
            is Int -> param(analyticsParam.key, analyticsParam.value.toLong())
            else -> param(analyticsParam.key, analyticsParam.value.toString())
        }
    }

    override fun logEvent(vararg events: AnalyticsEvent) {
        events.forEach { analyticsEvent ->
            firebaseAnalytics.logEvent(analyticsEvent.type) {
                analyticsEvent.extras.forEach { extra ->
                    param(extra)
                }
            }
        }
    }

    override fun setUserProperty(userProperty: UserProperty) {
        firebaseAnalytics.setUserProperty(userProperty.name, userProperty.value)
    }

    override fun enableAll(enabled: Boolean) {
        setGeneralAnalyticsEnabled(enabled)
        setCrashlyticsEnabled(enabled)
        setPerformanceEnabled(enabled)
    }

    override fun setGeneralAnalyticsEnabled(enabled: Boolean) {
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(enabled)
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        firebasePerformance.isPerformanceCollectionEnabled = enabled
    }
}