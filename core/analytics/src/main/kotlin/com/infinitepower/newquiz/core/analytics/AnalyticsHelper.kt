package com.infinitepower.newquiz.core.analytics

/**
 * Interface for logging analytics events.
 */
interface AnalyticsHelper {
    /**
     * Logs the given [events].
     *
     * @param events the events to log.
     * @see AnalyticsEvent
     */
    fun logEvent(vararg events: AnalyticsEvent)

    /**
     * Sets the user property to the given value.
     *
     * @param userProperty the user property to set.
     * @see UserProperty
     */
    fun setUserProperty(userProperty: UserProperty)
}