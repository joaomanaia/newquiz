package com.infinitepower.newquiz.home_presentation.login

import android.content.Context
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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
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
    val context = LocalContext.current
    val workManager = remember { WorkManager.getInstance(context) }

    val firebaseLogin = rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) {
        val request = OneTimeWorkRequestBuilder<CheckUserDBWorker>().build()
        workManager.enqueue(request)

        onBack()
    }

    val loginProviders = remember {
        // If Google Play Services is available, we can use Google Sign In
        if (isGooglePlayServicesAvailable(context)) {
            listOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder()
                    .setRequireName(true)
                    .build()
            )
        } else {
            listOf(AuthUI.IdpConfig.EmailBuilder().build())
        }
    }

    val signInIntent = remember {
        AuthUI
            .getInstance()
            .createSignInIntentBuilder()
            .setTheme(R.style.Theme_NewQuiz)
            .setAvailableProviders(loginProviders)
            .setLogo(R.mipmap.ic_launcher_round)
            .setIsSmartLockEnabled(true)
            .setAlwaysShowSignInMethodScreen(true)
            .build()
    }

    SideEffect {
        firebaseLogin.launch(signInIntent)
    }
}

private fun isGooglePlayServicesAvailable(context: Context): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
    return status == ConnectionResult.SUCCESS
}
