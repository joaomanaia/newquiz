package com.infinitepower.newquiz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.infinitepower.newquiz.core.database.model.DailyChallengeTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM daily_challenge_tasks ORDER BY endDate DESC")
    fun getAllTasksFlow(): Flow<List<DailyChallengeTaskEntity>>

    @Query("SELECT * FROM daily_challenge_tasks ORDER BY endDate DESC")
    suspend fun getAllTasks(): List<DailyChallengeTaskEntity>

    /**
     * Get all tasks that are available for the current date.
     * A task is available if the [currentTime] date is between the start date and end date of the task.
     *
     * @param currentTime The current date in milliseconds.
     */
    @Query(
        """
        SELECT * FROM daily_challenge_tasks
        WHERE startDate <= :currentTime AND endDate >= :currentTime
        ORDER BY endDate DESC
        """
    )
    suspend fun getAvailableTasks(currentTime: Long): List<DailyChallengeTaskEntity>

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 
            FROM daily_challenge_tasks 
            WHERE startDate <= :currentTime AND endDate >= :currentTime
        )
        """
    )
    suspend fun tasksAreAvailable(currentTime: Long): Boolean

    @Query("SELECT * FROM daily_challenge_tasks WHERE type = :type ORDER BY endDate DESC")
    suspend fun getTaskByType(type: String): DailyChallengeTaskEntity?

    @Insert
    suspend fun insertAll(vararg tasks: DailyChallengeTaskEntity)

    @Insert
    suspend fun insertAll(tasks: List<DailyChallengeTaskEntity>)

    @Update
    suspend fun update(vararg tasks: DailyChallengeTaskEntity)

    @Update
    suspend fun updateAll(tasks: List<DailyChallengeTaskEntity>)

    @Query("DELETE FROM daily_challenge_tasks")
    suspend fun deleteAll()
}
