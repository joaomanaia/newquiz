package com.infinitepower.newquiz.home_presentation.login.login_with_email

import android.util.Patterns
import androidx.annotation.Keep

@Keep
data class LoginWithEmailUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val emailExists: Boolean = false,
    val emailVerified: Boolean = false,
    val loading: Boolean = false,
    val loginCompleted: Boolean = false,
    val error: String? = null
) {
    val emailValid: Boolean
        get() = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val passwordValid: Boolean
        get() = password.length in 6..20

    val nameValid: Boolean
        get() = name.length in 2..20
}
