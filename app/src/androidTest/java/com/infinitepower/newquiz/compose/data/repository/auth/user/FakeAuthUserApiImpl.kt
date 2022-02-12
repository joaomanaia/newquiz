package com.infinitepower.newquiz.compose.data.repository.auth.user

import android.net.Uri
import androidx.core.net.toUri
import com.infinitepower.newquiz.compose.core.DEFAULT_USER_PHOTO
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import java.security.SecureRandom

class FakeAuthUserApiImpl : AuthUserApi {

    data class FakeAuthUser(
        val uid: String,
        val name: String,
        val photoUri: Uri
    )

    private var authUser: FakeAuthUser? = null

    override suspend fun updateAuthNewUser() {
        val uid = SecureRandom().nextInt().toString()
        val name = "user$uid"
        val photoUri = DEFAULT_USER_PHOTO.toUri()

        authUser = FakeAuthUser(uid, name, photoUri)
    }

    override val isSignedIn: Boolean
        get() = authUser != null

    override fun getUid() = authUser?.uid

    override fun getName() = authUser?.name

    override fun getPhotoUrl() = authUser?.photoUri
}