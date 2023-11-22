package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.core.user_services.model.User

interface UserService : GameResultTracker {
    /**
     * @return true if the user is available, false otherwise
     */
    suspend fun userAvailable(): Boolean

    suspend fun getUser(): User?

    suspend fun getUserDiamonds(): UInt

    /**
     * @param diamonds the amount of diamonds to add/remove
     */
    suspend fun addRemoveDiamonds(diamonds: Int)
}