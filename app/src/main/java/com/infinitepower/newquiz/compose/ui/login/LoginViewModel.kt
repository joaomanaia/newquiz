package com.infinitepower.newquiz.compose.ui.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.compose.R
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.data.remote.user.User
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import com.infinitepower.newquiz.compose.ui.login.manager.FirebaseAuthManager
import com.infinitepower.newquiz.compose.worker.user.CreateUserDBWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val workManager: WorkManager,
    authUserApi: AuthUserApi
) : ViewModel(), FirebaseAuthManager {
    companion object {
        private const val TAG = "LoginViewModel"
    }

    val userSignedIn = authUserApi.isSignedIn

    private fun updateAuthUser() {
        val createUserDBRequest = OneTimeWorkRequestBuilder<CreateUserDBWorker>().build()
        workManager.enqueue(createUserDBRequest)
    }

    private val _authResultCode = MutableStateFlow(AuthResultCode.NOT_APPLICABLE)
    val authResultCode = _authResultCode.asStateFlow()

    override fun buildLoginIntent(): Intent {
        val authUILayout = AuthMethodPickerLayout.Builder(R.layout.auth_ui)
            .setEmailButtonId(R.id.btn_email)
            .setGoogleButtonId(R.id.btn_google)
            .build()

        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(
                listOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )
            )
            .setTheme(R.style.Theme_NewQuiz_AppBar)
            .setAuthMethodPickerLayout(authUILayout)
            .build()
    }

    override fun onLoginResult(result: FirebaseAuthUIAuthenticationResult) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "onLoginResult triggered")
            Log.d(TAG, result.toString())

            val response = result.idpResponse
            if (result.resultCode == Activity.RESULT_OK) {
                updateAuthUser()

                _authResultCode.emit(AuthResultCode.OK)

                Log.d(TAG, "Login successful")
                return@launch
            }

            val userPressedBackButton = (response == null)
            if (userPressedBackButton) {
                _authResultCode.emit(AuthResultCode.CANCELLED)
                Log.d(TAG, "Login cancelled by user")
                return@launch
            }

            when (response?.error?.errorCode) {
                ErrorCodes.NO_NETWORK -> {
                    _authResultCode.emit(AuthResultCode.NO_NETWORK)

                    Log.d(TAG, "Login failed on network connectivity")
                }
                else -> {
                    Log.d(TAG, "Login failed")
                    _authResultCode.emit(AuthResultCode.ERROR)
                }
            }
        }
    }
}

enum class AuthResultCode {
    NOT_APPLICABLE,
    OK,
    CANCELLED,
    MERGED,
    NO_NETWORK,
    ERROR,
    LOGGING_OUT,
    LOGGED_OUT
}