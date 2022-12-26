package com.infinitepower.newquiz.online_services.ui.profile

import androidx.annotation.Keep
import com.infinitepower.newquiz.online_services.model.user.User

@Keep
data class ProfileScreenUiState(
    val user: User? = null,
    val loading: Boolean = true
)
