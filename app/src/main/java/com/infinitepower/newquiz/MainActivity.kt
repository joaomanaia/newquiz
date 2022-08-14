package com.infinitepower.newquiz

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.ump.*
import com.infinitepower.newquiz.core.navigation.AppNavigation
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private val consentInformation: ConsentInformation by lazy {
        UserMessagingPlatform.getConsentInformation(this)
    }

    private var consentForm: ConsentForm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            NewQuizTheme {
                val windowSize = calculateWindowSizeClass(activity = this)

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        navController = rememberNavController(),
                        modifier = Modifier.fillMaxSize(),
                        windowWidthSizeClass = windowSize.widthSizeClass,
                        windowHeightSizeClass = windowSize.heightSizeClass,
                        consentInformation = consentInformation
                    )
                }
            }
        }

        val debugSettings = ConsentDebugSettings
            .Builder(this)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .build()


        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings)
            .build()

        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                if (consentInformation.isConsentFormAvailable) {
                    loadConsentForm()
                }
            },
            {}
        )
    }

    private fun loadConsentForm() {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm ->
                this@MainActivity.consentForm = consentForm

                if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED
                    || consentInformation.consentStatus == ConsentInformation.ConsentStatus.UNKNOWN) {
                    consentForm.show(this) {
                        loadConsentForm()
                    }
                }
            },
            {}
        )
    }
}