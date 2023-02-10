package com.infinitepower.newquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.infinitepower.newquiz.core.navigation.AppNavigation
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @Inject lateinit var authUserRepository: AuthUserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            NewQuizTheme {
                val windowSize = calculateWindowSizeClass(activity = this)

                val signedIn by authUserRepository.isSignedInFlow.collectAsState(initial = false)

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        navController = rememberNavController(),
                        modifier = Modifier.fillMaxSize(),
                        windowSizeClass = windowSize,
                        signedIn = signedIn
                    )
                }
            }
        }
    }
}