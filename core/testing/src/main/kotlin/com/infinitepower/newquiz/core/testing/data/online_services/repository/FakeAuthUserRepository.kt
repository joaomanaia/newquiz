package com.infinitepower.newquiz.core.testing.data.online_services.repository

import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthUserRepository @Inject constructor() : AuthUserRepository {
    private data class FakeAuthUser(
        val uid: String,
        val name: String,
        val photoUrl: URI
    )

    private val fakeAuthUser = MutableStateFlow<FakeAuthUser?>(null)

    override val isSignedIn: Boolean
        get() = fakeAuthUser.value != null

    override val isSignedInFlow: Flow<Boolean>
        get() = fakeAuthUser.map { it != null }

    override val uid: String?
        get() = fakeAuthUser.value?.uid

    override val name: String?
        get() = fakeAuthUser.value?.name

    override val photoUrl: URI?
        get() = fakeAuthUser.value?.photoUrl


    override suspend fun refreshAuthUser() {
        // Do nothing
    }

    override fun signOut() {
        fakeAuthUser.value = null
    }
}