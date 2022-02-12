package com.infinitepower.newquiz.compose.ui.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.infinitepower.newquiz.compose.ui.destinations.LoginScreenDestination
import com.infinitepower.newquiz.compose.ui.destinations.MainScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun LoginScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: LoginViewModel = hiltViewModel()

    val authResultCode by viewModel.authResultCode.collectAsState()

    val signIn = rememberLauncherForActivityResult(
        contract = viewModel.buildLoginActivityResult()
    ) { result ->
        if (result != null) {
            viewModel.onLoginResult(result)
        }
    }

    when (authResultCode) {
        AuthResultCode.OK -> {
            navigator.navigate(MainScreenDestination) {
                launchSingleTop = true
                popUpTo(LoginScreenDestination.route) {
                    inclusive = true
                }
            }
        }
        AuthResultCode.CANCELLED -> navigator.popBackStack()
        else -> {
            if (!viewModel.userSignedIn) {
                LaunchedEffect(key1 = true) {
                    signIn.launch(viewModel.buildLoginIntent())
                }
            }
        }
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