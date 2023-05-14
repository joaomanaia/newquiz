package com.infinitepower.newquiz.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.infinitepower.newquiz.core.navigation.AppNavigation
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import com.infinitepower.newquiz.ui.components.DataCollectionConsentDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            NewQuizTheme {
                val windowSize = calculateWindowSizeClass(activity = this)

                val mainViewModel = hiltViewModel<MainViewModel>()

                val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        navController = rememberNavController(),
                        modifier = Modifier.fillMaxSize(),
                        windowSizeClass = windowSize,
                        signedIn = uiState.signedIn,
                        showLoginCard = uiState.showLoginCard,
                        dailyChallengeClaimCount = uiState.dailyChallengeClaimableCount,
                        onSignDismissClick = { mainViewModel.onEvent(MainScreenUiEvent.DismissLoginCard) }
                    )

                    if (uiState.dialogConsent == DataAnalyticsConsentState.NONE && !uiState.consentStateLoading) {
                        DataCollectionConsentDialog(
                            onAgreeClick = { mainViewModel.onEvent(MainScreenUiEvent.OnAgreeDisagreeClick(true)) },
                            onDisagreeClick = { mainViewModel.onEvent(MainScreenUiEvent.OnAgreeDisagreeClick(false)) }
                        )
                    }
                }
            }
        }
    }
}
