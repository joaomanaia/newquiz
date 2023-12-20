package com.infinitepower.newquiz.core.analytics

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Implementation of [AnalyticsHelper] which does nothing. Useful for tests and previews.
 */
object NoOpAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(vararg events: AnalyticsEvent) = Unit

    override fun setUserProperty(userProperty: UserProperty) = Unit

    override fun setGeneralAnalyticsEnabled(enabled: Boolean) = Unit

    override fun setCrashlyticsEnabled(enabled: Boolean) = Unit

    override fun setPerformanceEnabled(enabled: Boolean) = Unit

    override val showDataAnalyticsConsentDialog: Flow<Boolean> = emptyFlow()

    override suspend fun updateDataConsent(agreed: Boolean) = Unit
}