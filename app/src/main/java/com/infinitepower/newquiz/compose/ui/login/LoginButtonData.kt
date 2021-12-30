package com.infinitepower.newquiz.compose.ui.login

import androidx.annotation.DrawableRes
import com.infinitepower.newquiz.compose.R

sealed class LoginButtonData(
    val key: String,
    val name: String,
    @DrawableRes val icon: Int
) {
    object Anonymous : LoginButtonData(
        key = "anonymous",
        name = "Continuar como convidado",
        icon = R.drawable.fui_ic_anonymous_white_24dp
    )

    data class Email(
        val requiredName: Boolean = true
    ) : LoginButtonData(
        key = "email",
        name = "Continuar com o email",
        icon = R.drawable.fui_ic_mail_white_24dp
    )

    object Google : LoginButtonData(
        key = "google",
        name = "Continuar com o google",
        icon = R.drawable.fui_ic_googleg_color_24dp
    )
}