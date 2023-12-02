package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.core.user_services.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

typealias XpEarnedByDays = Map<LocalDate, Int>

interface UserService : GameResultTracker, XpManager {
    /**
     * @return true if the user is available, false otherwise
     */
    suspend fun userAvailable(): Boolean

    suspend fun getUser(): User?

    suspend fun getUserDiamonds(): UInt

    fun getUserDiamondsFlow(): Flow<UInt>

    /**
     * @param diamonds the amount of diamonds to add/remove
     */
    suspend fun addRemoveDiamonds(diamonds: Int)
}