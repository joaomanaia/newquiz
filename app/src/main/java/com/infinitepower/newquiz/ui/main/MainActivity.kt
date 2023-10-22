package com.infinitepower.newquiz.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.navigation.AppNavigation
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import com.infinitepower.newquiz.ui.components.DataCollectionConsentDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainScreenUiState by mutableStateOf(MainScreenUiState())

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .uiState
                    .onEach {
                        uiState = it
                    }.collect()
            }
        }

        // Keep the splash screen until the uiState is loaded
        splashScreen.setKeepOnScreenCondition { uiState.loading }

        setContent {
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper
            ) {
                NewQuizTheme(
                    animationsEnabled = uiState.animationsEnabled,
                ) {
                    val windowSize = calculateWindowSizeClass(activity = this)

                    Surface(
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation(
                            navController = rememberNavController(),
                            modifier = Modifier.fillMaxSize(),
                            windowSizeClass = windowSize,
                            remoteConfig = remoteConfig,
                            signedIn = uiState.signedIn,
                            showLoginCard = uiState.showLoginCard,
                            dailyChallengeClaimCount = uiState.dailyChallengeClaimableCount,
                            onSignDismissClick = { viewModel.onEvent(MainScreenUiEvent.DismissLoginCard) }
                        )

                        if (uiState.dialogConsent == DataAnalyticsConsentState.NONE && !uiState.loading) {
                            DataCollectionConsentDialog(
                                onAgreeClick = { viewModel.onEvent(MainScreenUiEvent.OnAgreeDisagreeClick(true)) },
                                onDisagreeClick = { viewModel.onEvent(MainScreenUiEvent.OnAgreeDisagreeClick(false)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
