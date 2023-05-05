package com.infinitepower.newquiz.online_services.ui.login.login_with_email

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase.ui.auth.R
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun LoginWithEmailScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginWithEmailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = uiState.loginCompleted) {
        if (uiState.loginCompleted) {
            navigator.popBackStack()
        }
    }

    LoginWithEmailScreenImpl(
        uiState = uiState,
        onBackClick = navigator::popBackStack,
        onEvent = viewModel::onEvent
    )
}

@Composable
@ExperimentalMaterial3Api
fun LoginWithEmailScreenImpl(
    uiState: LoginWithEmailUiState,
    onBackClick: () -> Unit,
    onEvent: (event: LoginWithEmailUiEvent) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.fui_sign_in_with_email))
                },
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = { onEvent(LoginWithEmailUiEvent.EmailChanged(it)) },
                label = { Text(text = stringResource(id = CoreR.string.email)) },
                singleLine = true,
                maxLines = 1,
                enabled = !uiState.loading,
                isError = !uiState.emailValid,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    // If email is verified, go to password field, otherwise send verification email
                    imeAction = if (uiState.emailVerified) ImeAction.Next else ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    // If email is not verified, verify email exists
                    onSend = { onEvent(LoginWithEmailUiEvent.VerifyEmailClick) }
                )
            )

            if (uiState.emailVerified) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.password,
                    onValueChange = { onEvent(LoginWithEmailUiEvent.PasswordChanged(it)) },
                    label = { Text(text = stringResource(id = CoreR.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    maxLines = 1,
                    enabled = !uiState.loading,
                    isError = !uiState.passwordValid,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = if (uiState.emailExists) ImeAction.Send else ImeAction.Next
                    )
                )

                if (!uiState.emailExists) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.name,
                        onValueChange = { onEvent(LoginWithEmailUiEvent.NameChanged(it)) },
                        label = { Text(text = stringResource(id = CoreR.string.name)) },
                        singleLine = true,
                        maxLines = 1,
                        enabled = !uiState.loading,
                        isError = !uiState.nameValid,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Send
                        )
                    )
                }
            }

            LoginButton(
                loading = uiState.loading,
                emailValid = uiState.emailValid,
                passwordValid = uiState.passwordValid,
                nameValid = uiState.nameValid,
                emailVerified = uiState.emailVerified,
                emailExists = uiState.emailExists,
                onLoginClick = { onEvent(LoginWithEmailUiEvent.LoginButtonClicked) },
                onNextClick = { onEvent(LoginWithEmailUiEvent.VerifyEmailClick) }
            )
        }
    }
}

@Composable
private fun LoginButton(
    modifier: Modifier = Modifier,
    loading: Boolean,
    emailValid: Boolean,
    passwordValid: Boolean,
    nameValid: Boolean,
    emailExists: Boolean,
    emailVerified: Boolean,
    onLoginClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    if (emailVerified) {
        val text = if (emailExists) {
            stringResource(id = CoreR.string.sign_in)
        } else {
            stringResource(id = CoreR.string.sign_up)
        }

        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = onLoginClick,
            enabled = passwordValid && !loading && (emailExists || nameValid)
        ) {
            Text(text = text)
        }
    } else {
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = onNextClick,
            enabled = emailValid && !loading
        ) {
            Text(text = stringResource(id = CoreR.string.next))
        }
    }
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun LoginWithEmailScreenPreview() {
    NewQuizTheme {
        Surface {
            LoginWithEmailScreenImpl(
                uiState = LoginWithEmailUiState(
                    email = "example@example.com",
                    password = "password",
                    name = "New Quiz",
                    emailVerified = true
                ),
                onBackClick = {},
                onEvent = {}
            )
        }
    }
}
