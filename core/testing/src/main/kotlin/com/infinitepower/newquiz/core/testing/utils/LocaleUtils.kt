package com.infinitepower.newquiz.core.testing.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.annotations.TestOnly
import java.util.Locale

@TestOnly
@VisibleForTesting
@Suppress("DEPRECATION")
@SuppressLint("AppBundleLocaleChanges")
fun setTestDeviceLocale(
    context: Context,
    locale: Locale = Locale.ENGLISH
) {
    context.resources.apply {
        val config = Configuration(configuration)

        context.createConfigurationContext(configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, displayMetrics)
    }
}

@TestOnly
@Composable
@VisibleForTesting
@Suppress("ComposableNaming")
fun setTestDeviceLocale(
    locale: Locale = Locale.ENGLISH
) {
    setTestDeviceLocale(LocalContext.current, locale)
}
