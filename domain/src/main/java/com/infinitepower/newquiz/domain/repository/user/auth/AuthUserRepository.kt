package com.infinitepower.newquiz.domain.repository.user.auth

import android.net.Uri

interface AuthUserRepository {
    val isSignedIn: Boolean

    val uid: String?

    val name: String?

    val photoUrl: Uri?

    suspend fun refreshAuthUser()
}