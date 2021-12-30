package com.infinitepower.newquiz.compose.data.remote.auth.user

import android.net.Uri
import androidx.core.net.toUri

class FakeAuthUserApiImpl : AuthUserApi {

    private data class AuthUser(
        val uid: String?,
        val name: String?,
        val photoUrl: Uri?
    )

    private var authUser: AuthUser? = null

    override suspend fun updateAuthNewUser() {
        authUser = AuthUser(
            uid = "uid",
            name = "name",
            photoUrl = "photoUrl".toUri()
        )
    }

    override val isSignedIn = authUser != null

    override fun getUid() = authUser?.uid

    override fun getName() = authUser?.name

    override fun getPhotoUrl() = authUser?.photoUrl
}