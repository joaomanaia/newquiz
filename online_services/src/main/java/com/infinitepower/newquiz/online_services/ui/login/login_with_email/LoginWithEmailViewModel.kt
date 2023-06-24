package com.infinitepower.newquiz.online_services.ui.login.login_with_email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.online_services.core.login.LoginCore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginWithEmailViewModel @Inject constructor(
    private val loginCore: LoginCore
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginWithEmailUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginWithEmailUiEvent) {
        when (event) {
            is LoginWithEmailUiEvent.EmailChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        email = event.email,
                        emailExists = false,
                        emailVerified = false
                    )
                }
            }
            is LoginWithEmailUiEvent.PasswordChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(password = event.password)
                }
            }
            is LoginWithEmailUiEvent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = event.name)
                }
            }
            is LoginWithEmailUiEvent.LoginButtonClicked -> loginRegister()
            is LoginWithEmailUiEvent.VerifyEmailClick -> checkEmailExists()
        }
    }

    private fun loginRegister() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = uiState.first()

            if (state.emailExists) {
                loginWithEmail(state.email, state.password)
            } else {
                registerWithEmail(state.email, state.password, state.name)
            }
        }
    }

    private suspend fun loginWithEmail(
        email: String,
        password: String
    ) {
        loginCore
            .loginWithEmail(email, password)
            .collect { res ->
                _uiState.update { currentState ->
                    currentState.copy(
                        loading = res.isLoading(),
                        loginCompleted = res.isSuccess(),
                        error = res.message
                    )
                }
            }
    }

    private suspend fun registerWithEmail(
        email: String,
        password: String,
        name: String
    ) {
        loginCore
            .registerWithEmail(email, password, name)
            .collect { res ->
                _uiState.update { currentState ->
                    currentState.copy(
                        loading = res.isLoading(),
                        loginCompleted = res.isSuccess(),
                        error = res.message
                    )
                }
            }
    }

    private fun checkEmailExists() {
        viewModelScope.launch(Dispatchers.IO) {
            val email = uiState.first().email

            loginCore
                .emailExists(email)
                .collect { res ->
                    if (res.isLoading()) {
                        _uiState.update { currentState ->
                            currentState.copy(loading = true)
                        }
                    } else {
                        val emailExists = res is Resource.Success && res.data == true

                        _uiState.update { currentState ->
                            currentState.copy(
                                emailExists = emailExists,
                                emailVerified = true,
                                loading = false
                            )
                        }
                    }
                }
        }
    }
}