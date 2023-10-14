package com.infinitepower.newquiz.core.testing.data.online_services.repository

import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.online_services.core.login.LoginCore
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLoginCoreImpl @Inject constructor() : LoginCore {
    private data class LoginData(val email: String, val password: String, val name: String)

    private val loginData = mutableListOf<LoginData>()

    override fun loginWithEmail(email: String, password: String): FlowResource<Unit> = flow {
        emit(Resource.Loading())

        if (loginData.any { it.email == email && it.password == password }) {
            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error("Invalid email or password"))
        }
    }

    override fun registerWithEmail(
        email: String,
        password: String,
        name: String
    ): FlowResource<Unit> = flow {
        emit(Resource.Loading())

        if (loginData.any { it.email == email }) {
            emit(Resource.Error("Email already exists"))
        } else {
            loginData.add(LoginData(email, password, name))
            emit(Resource.Success(Unit))
        }
    }

    override fun emailExists(email: String): FlowResource<Boolean> = flow {
        emit(Resource.Loading())

        emit(Resource.Success(loginData.any { it.email == email }))
    }
}