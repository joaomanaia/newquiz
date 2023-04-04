package com.infinitepower.newquiz.online_services.core.login

import com.infinitepower.newquiz.core.common.FlowResource

interface LoginCore {
    fun loginWithEmail(
        email: String,
        password: String
    ): FlowResource<Unit>

    fun registerWithEmail(
        email: String,
        password: String,
        name: String
    ): FlowResource<Unit>

    fun emailExists(email: String): FlowResource<Boolean>
}