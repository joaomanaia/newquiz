package com.infinitepower.newquiz.home_presentation.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.online_services.core.worker.CheckUserDBWorker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun LoginScreen(
    navigator: DestinationsNavigator
) {
    LoginScreenImpl(
        onBack = navigator::popBackStack
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("LoginScreen")
    }
}

@Composable
private fun LoginScreenImpl(
    onBack: () -> Unit
) {
    val localContext = LocalContext.current

    val workManager = remember { WorkManager.getInstance(localContext) }

    val firebaseLogin = rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) {
        val request = OneTimeWorkRequestBuilder<CheckUserDBWorker>().build()
        workManager.enqueue(request)

        onBack()
    }

    val signInIntent = remember {
        AuthUI
            .getInstance()
            .createSignInIntentBuilder()
            .setTheme(R.style.Theme_NewQuiz)
            .setAvailableProviders(
                listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build()
                )
            ).setLogo(R.mipmap.ic_launcher_round)
            .setIsSmartLockEnabled(true)
            .setAlwaysShowSignInMethodScreen(true)
            .build()
    }

    SideEffect {
        firebaseLogin.launch(signInIntent)
    }
}