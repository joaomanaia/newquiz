package com.infinitepower.newquiz.online_services.domain.user.auth

import kotlinx.coroutines.flow.Flow
import java.net.URI

interface AuthUserRepository {
    val isSignedIn: Boolean

    val isSignedInFlow: Flow<Boolean>

    val uid: String?

    val name: String?

    val photoUrl: URI?

    suspend fun refreshAuthUser()

    fun signOut()
}