package com.infinitepower.newquiz.home_presentation.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.AppLogo
import com.infinitepower.newquiz.home_presentation.destinations.LoginWithEmailScreenDestination
import com.infinitepower.newquiz.online_services.core.util.isGooglePlayServicesAvailable
import com.infinitepower.newquiz.online_services.core.worker.CheckUserDBWorker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun LoginScreen(
    windowSizeClass: WindowSizeClass,
    navigator: DestinationsNavigator
) {
    LoginScreenImpl(
        windowSizeClass = windowSizeClass,
        onBack = navigator::popBackStack,
        navigateToLoginWithEmail = { navigator.navigate(LoginWithEmailScreenDestination) }
    )
}

@Composable
private fun LoginScreenImpl(
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit,
    navigateToLoginWithEmail: () -> Unit
) {
    val context = LocalContext.current

    // If Google Play Services is available, use FirebaseUI to login
    if (isGooglePlayServicesAvailable(context)) {
        LoginWithFirebaseUi(onBack = onBack)
    } else {
        LoginWithoutPlayServices(
            windowSizeClass = windowSizeClass,
            navigateToLoginWithEmail = navigateToLoginWithEmail
        )
    }
}

/**
 * Normal login using FirebaseUI
 */
@Composable
private fun LoginWithFirebaseUi(
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
        listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder()
                .setRequireName(true)
                .build()
        )
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

@Composable
private fun LoginWithoutPlayServices(
    windowSizeClass: WindowSizeClass,
    navigateToLoginWithEmail: () -> Unit
) {
    // val googleIconPainter = painterResource(id = com.firebase.ui.auth.R.drawable.fui_ic_googleg_color_24dp)

    LoginWithoutPlayServicesContainer(
        windowSizeClass = windowSizeClass,
        loginHeadline = {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        appLogo = { AppLogo() },
        loginButtons = {
            /*
            LoginButton(
                modifier = Modifier.width(300.dp),
                onClick = onLoginSuccess,
                text = stringResource(id = com.firebase.ui.auth.R.string.fui_sign_in_with_google),
                icon = {
                    Icon(
                        painter = googleIconPainter,
                        contentDescription = stringResource(id = com.firebase.ui.auth.R.string.fui_sign_in_with_google)
                    )
                }
            )
             */
            LoginButton(
                modifier = Modifier.width(300.dp),
                onClick = navigateToLoginWithEmail,
                text = stringResource(id = com.firebase.ui.auth.R.string.fui_sign_in_with_email),
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = stringResource(id = com.firebase.ui.auth.R.string.fui_sign_in_with_email)
                    )
                }
            )
        }
    )
}

@Composable
fun LoginWithoutPlayServicesContainer(
    windowSizeClass: WindowSizeClass,
    loginHeadline: @Composable () -> Unit,
    appLogo: @Composable () -> Unit,
    loginButtons: @Composable ColumnScope.() -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.extraLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                appLogo()
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spaceMedium)
            ) {
                loginHeadline()
                Spacer(modifier = Modifier.height(spaceMedium))
                loginButtons()
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            appLogo()
            Spacer(modifier = Modifier.height(spaceMedium))
            loginHeadline()
            Spacer(modifier = Modifier.weight(1f))
            loginButtons()
        }
    }
}

@Composable
private fun LoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    icon: @Composable () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        icon()
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        Text(text = text)
    }
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun LoginWithoutPlayServicesPreview() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        Surface {
            LoginWithoutPlayServices(
                windowSizeClass = windowSizeClass,
                navigateToLoginWithEmail = {}
            )
        }
    }
}
