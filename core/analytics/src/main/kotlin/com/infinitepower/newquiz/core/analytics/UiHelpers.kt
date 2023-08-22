package com.infinitepower.newquiz.core.analytics

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * A local composition that provides an [AnalyticsHelper] instance.
 */
val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    LocalDebugAnalyticsHelper()
}
