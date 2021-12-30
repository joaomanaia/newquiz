package com.infinitepower.newquiz.compose.ui.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.UserProfileChangeRequest
import com.infinitepower.newquiz.compose.R
import com.infinitepower.newquiz.compose.core.navigation.Screen
import com.infinitepower.newquiz.compose.ui.login.components.LoginButtons
import java.security.SecureRandom

private val providers = listOf(
    AuthUI.IdpConfig.EmailBuilder()
        .setRequireName(true)
        .build(),
    AuthUI.IdpConfig.GoogleBuilder().build(),
    AuthUI.IdpConfig.FacebookBuilder()
        .setPermissions(listOf("email", "public_profile"))
        .build(),
    AuthUI.IdpConfig.AnonymousBuilder().build()
)

private val signInIntent = AuthUI.getInstance()
    .createSignInIntentBuilder()
    .setAvailableProviders(providers)
    .setTosAndPrivacyPolicyUrls(
        "https://infinitepower.tk/newquiz/terms",
        "https://infinitepower.tk/newquiz/privacy"
    )
    .setLogo(R.mipmap.ic_launcher_round)
    .setIsSmartLockEnabled(false)
    .build()

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val signIn = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val response = IdpResponse.fromResultIntent(it.data)

        if (it.resultCode == Activity.RESULT_OK) {
            if (response != null) {
                if (response.isNewUser) {
                    viewModel.updateAuthNewUser()
                    viewModel.createNewUser()
                }

                navController.navigate(
                    Screen.MainScreen.route,
                ) {
                    launchSingleTop = true
                    popUpTo(Screen.LoginScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    SideEffect {
        signIn.launch(signInIntent)
    }
}

/*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.mipmap.ic_launcher_round),
            contentDescription = "NewQuiz Logo",
            modifier = Modifier
                .padding(top = 128.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.weight(1f))
        LoginButtons(
            buttons = providers,
            onClick = {}
        )
        Spacer(modifier = Modifier.padding(bottom = 32.dp))
    }
}

private val providers = listOf(
    LoginButtonData.Google,
    LoginButtonData.Email(
        requiredName = true
    ),
    LoginButtonData.Anonymous
)
     */