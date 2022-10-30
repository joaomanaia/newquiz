package com.infinitepower.newquiz.home_presentation.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.infinitepower.newquiz.core.R
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
}

@Composable
private fun LoginScreenImpl(
    onBack: () -> Unit
) {
    val firebaseLogin = rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) {
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