package com.infinitepower.newquiz.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.infinitepower.newquiz.core.datastore.common.DataAnalyticsCommon
import com.infinitepower.newquiz.core.datastore.di.DataAnalyticsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebasePerformance: FirebasePerformance,
    @DataAnalyticsDataStoreManager private val dataAnalyticsDataStoreManager: DataStoreManager,
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

    override fun setGeneralAnalyticsEnabled(enabled: Boolean) {
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(enabled)
    }

    override fun setPerformanceEnabled(enabled: Boolean) {
        firebasePerformance.isPerformanceCollectionEnabled = enabled
    }

    override val showDataAnalyticsConsentDialog: Flow<Boolean>
        get() = dataAnalyticsDataStoreManager
            .getPreferenceFlow(DataAnalyticsCommon.DataAnalyticsConsent)
            .map { consentStr ->
                val consent = DataAnalyticsConsentState.valueOf(consentStr)

                consent == DataAnalyticsConsentState.NONE
            }

    override suspend fun updateDataConsent(agreed: Boolean) {
        val consentState = if (agreed) {
            DataAnalyticsConsentState.AGREED
        } else {
            DataAnalyticsConsentState.DISAGREED
        }

        dataAnalyticsDataStoreManager.editPreference(
            key = DataAnalyticsCommon.DataAnalyticsConsent.key,
            newValue = consentState.name
        )

        // Update the parent data analytics settings
        dataAnalyticsDataStoreManager.editPreference(
            key = DataAnalyticsCommon.GloballyAnalyticsCollectionEnabled.key,
            newValue = agreed
        )

        // Enable or disable the individual analytics settings,
        // and update the datastore with the new values

        setGeneralAnalyticsEnabled(agreed)
        dataAnalyticsDataStoreManager.editPreference(
            key = DataAnalyticsCommon.GeneralAnalyticsEnabled.key,
            newValue = agreed
        )

        setCrashlyticsEnabled(agreed)
        dataAnalyticsDataStoreManager.editPreference(
            key = DataAnalyticsCommon.CrashlyticsEnabled.key,
            newValue = agreed
        )

        setPerformanceEnabled(agreed)
        dataAnalyticsDataStoreManager.editPreference(
            key = DataAnalyticsCommon.PerformanceMonitoringEnabled.key,
            newValue = agreed
        )
    }
}