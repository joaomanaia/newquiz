package com.infinitepower.newquiz.compose.data.repository.auth.user

import androidx.core.net.toUri
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.compose.core.DEFAULT_USER_PHOTO
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import kotlinx.coroutines.tasks.await
import java.security.SecureRandom

class AuthUserApiImpl : AuthUserApi {
    override suspend fun updateAuthNewUser() {
        val auth = Firebase.auth

        val name = auth.currentUser?.displayName ?: "user${SecureRandom().nextInt()}"
        val photoUri = auth.currentUser?.photoUrl ?: DEFAULT_USER_PHOTO.toUri()

        val requestConfig = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(photoUri)
            .build()

        auth.currentUser?.updateProfile(requestConfig)?.await()
    }

    override val isSignedIn: Boolean
        get() = Firebase.auth.currentUser != null

    override fun getUid() = Firebase.auth.currentUser?.uid

    override fun getName() = Firebase.auth.currentUser?.displayName

    override fun getPhotoUrl() = Firebase.auth.currentUser?.photoUrl
}