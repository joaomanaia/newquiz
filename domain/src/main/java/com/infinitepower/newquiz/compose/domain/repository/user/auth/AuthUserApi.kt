package com.infinitepower.newquiz.compose.domain.repository.user.auth

import android.net.Uri

interface AuthUserApi {
    suspend fun updateAuthNewUser()

    val isSignedIn: Boolean

    fun getUid(): String?

    fun getName(): String?

    fun getPhotoUrl(): Uri?
}