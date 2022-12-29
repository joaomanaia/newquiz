package com.infinitepower.newquiz.domain.repository.user.auth

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface AuthUserRepository {
    val isSignedIn: Boolean

    val isSignedInFlow: Flow<Boolean>

    val uid: String?

    val name: String?

    val photoUrl: Uri?

    suspend fun refreshAuthUser()

    fun signOut()
}