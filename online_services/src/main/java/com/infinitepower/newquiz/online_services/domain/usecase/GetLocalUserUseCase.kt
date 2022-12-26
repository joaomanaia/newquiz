package com.infinitepower.newquiz.online_services.domain.usecase

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.online_services.model.user.User
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLocalUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): FlowResource<User> = flow {
        try {
            emit(Resource.Loading())

            val user = userRepository.getLocalUser() ?: throw NullPointerException("User not found")

            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error while loading user"))
        }
    }
}