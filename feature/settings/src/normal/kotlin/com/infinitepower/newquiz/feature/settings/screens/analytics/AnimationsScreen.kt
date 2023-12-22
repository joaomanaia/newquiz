package com.infinitepower.newquiz.feature.settings.screens.analytics

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.datastore.common.DataAnalyticsCommon
import com.infinitepower.newquiz.core.datastore.common.dataAnalyticsDataStore
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.PreferencesDatastoreManager
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.feature.settings.screens.PreferenceScreen
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import kotlinx.coroutines.launch

@Composable
fun rememberDataAnalyticsDataStoreManager(): DataStoreManager {
    val context = LocalContext.current
    val dataStore = context.dataAnalyticsDataStore

    return remember {
        PreferencesDatastoreManager(dataStore)
    }
}

@Composable
@ExperimentalMaterial3Api
internal fun AnalyticsScreen(
    modifier: Modifier = Modifier,
    isScreenExpanded: Boolean,
    onBackClick: () -> Unit,
) {
    val dataStoreManager = rememberDataAnalyticsDataStoreManager()
    val scope = rememberCoroutineScope()

    val analyticsHelper = LocalAnalyticsHelper.current

    val items = listOf(
        // Global analytics dependency
        Preference.PreferenceItem.SwitchPreference(
            request = DataAnalyticsCommon.GloballyAnalyticsCollectionEnabled,
            title = stringResource(id = CoreR.string.analytics_collection_enabled),
            onCheckChange = { enabled ->
                scope.launch {
                    enableLoggingAnalytics(
                        enabled = enabled,
                        dataAnalyticsDataStoreManager = dataStoreManager,
                        analyticsHelper = analyticsHelper
                    )
                }
            },
            primarySwitch = true
        ),
        Preference.PreferenceItem.SwitchPreference(
            request = DataAnalyticsCommon.GeneralAnalyticsEnabled,
            title = stringResource(id = CoreR.string.general_analytics_enabled),
            summary = stringResource(id = CoreR.string.general_analytics_description_enabled),
            dependency = listOf(DataAnalyticsCommon.GloballyAnalyticsCollectionEnabled),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Analytics,
                    contentDescription = stringResource(id = CoreR.string.general_analytics_enabled)
                )
            },
            onCheckChange = analyticsHelper::setGeneralAnalyticsEnabled
        ),
        Preference.PreferenceItem.SwitchPreference(
            request = DataAnalyticsCommon.CrashlyticsEnabled,
            title = stringResource(id = CoreR.string.crash_analytics_enabled),
            summary = stringResource(id = CoreR.string.crash_analytics_description_enabled),
            dependency = listOf(DataAnalyticsCommon.GloballyAnalyticsCollectionEnabled),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.BugReport,
                    contentDescription = stringResource(id = CoreR.string.crash_analytics_enabled)
                )
            },
            onCheckChange = analyticsHelper::setCrashlyticsEnabled
        ),
        Preference.PreferenceItem.SwitchPreference(
            request = DataAnalyticsCommon.PerformanceMonitoringEnabled,
            title = stringResource(id = CoreR.string.performance_monitoring_enabled),
            summary = stringResource(id = CoreR.string.performance_monitoring_description_enabled),
            dependency = listOf(DataAnalyticsCommon.GloballyAnalyticsCollectionEnabled),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.MonitorHeart,
                    contentDescription = stringResource(id = CoreR.string.performance_monitoring_enabled)
                )
            },
            onCheckChange = analyticsHelper::setPerformanceEnabled
        )
    )

    PreferenceScreen(
        modifier = modifier,
        title = stringResource(id = CoreR.string.analytics),
        items = items,
        dataStoreManager = dataStoreManager,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick
    )
}

private suspend fun enableLoggingAnalytics(
    enabled: Boolean,
    dataAnalyticsDataStoreManager: DataStoreManager,
    analyticsHelper: AnalyticsHelper
) {
    val consentState = if (enabled) {
        DataAnalyticsConsentState.AGREED
    } else {
        DataAnalyticsConsentState.DISAGREED
    }

    dataAnalyticsDataStoreManager.editPreference(
        key = DataAnalyticsCommon.DataAnalyticsConsent.key,
        newValue = consentState.name
    )

    // Enable general analytics
    val generalEnabled = dataAnalyticsDataStoreManager.getPreference(DataAnalyticsCommon.GeneralAnalyticsEnabled)
    analyticsHelper.setGeneralAnalyticsEnabled(generalEnabled && enabled)

    // Enable crashlytics
    val crashlyticsEnabled = dataAnalyticsDataStoreManager.getPreference(DataAnalyticsCommon.CrashlyticsEnabled)
    analyticsHelper.setCrashlyticsEnabled(crashlyticsEnabled && enabled)

    // Enable performance monitoring
    val performanceMonitoringEnabled = dataAnalyticsDataStoreManager.getPreference(
        DataAnalyticsCommon.PerformanceMonitoringEnabled
    )
    analyticsHelper.setPerformanceEnabled(performanceMonitoringEnabled && enabled)
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun AnalyticsScreenPreview() {
    NewQuizTheme {
        Surface {
            AnalyticsScreen(
                modifier = Modifier.padding(16.dp),
                isScreenExpanded = true,
                onBackClick = {}
            )
        }
    }
}
