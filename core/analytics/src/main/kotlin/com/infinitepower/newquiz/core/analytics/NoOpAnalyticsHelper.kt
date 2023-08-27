package com.infinitepower.newquiz.core.analytics

/**
 * Implementation of [AnalyticsHelper] which does nothing. Useful for tests and previews.
 */
object NoOpAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(vararg events: AnalyticsEvent) = Unit

    override fun setUserProperty(userProperty: UserProperty) = Unit

    override fun enableAll(enabled: Boolean) = Unit

    override fun setGeneralAnalyticsEnabled(enabled: Boolean) = Unit

    override fun setCrashlyticsEnabled(enabled: Boolean) = Unit

    override fun setPerformanceEnabled(enabled: Boolean) = Unit
}