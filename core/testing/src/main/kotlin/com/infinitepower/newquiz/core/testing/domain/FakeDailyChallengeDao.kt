package com.infinitepower.newquiz.core.testing.domain

import com.infinitepower.newquiz.core.database.dao.DailyChallengeDao
import com.infinitepower.newquiz.core.database.model.DailyChallengeTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class FakeDailyChallengeDao : DailyChallengeDao {
    private val savedTasks = MutableStateFlow<List<DailyChallengeTaskEntity>>(emptyList())

    override fun getAllTasksFlow(): Flow<List<DailyChallengeTaskEntity>> = savedTasks

    override suspend fun getAllTasks(): List<DailyChallengeTaskEntity> {
        return savedTasks.first()
    }

    override suspend fun getAvailableTasks(currentTime: Long): List<DailyChallengeTaskEntity> {
        return savedTasks.first()
            .filter { task -> task.startDate <= currentTime && task.endDate >= currentTime }
    }

    override suspend fun tasksAreAvailable(currentTime: Long): Boolean {
        return savedTasks.first()
            .any { task -> task.startDate <= currentTime && task.endDate >= currentTime }
    }

    override suspend fun getTaskByType(type: String): DailyChallengeTaskEntity? {
        return savedTasks.first().find { task -> task.type == type }
    }

    override suspend fun insertAll(vararg tasks: DailyChallengeTaskEntity) {
        insertAll(tasks.toList())
    }

    override suspend fun insertAll(tasks: List<DailyChallengeTaskEntity>) {
        savedTasks.emit(
            savedTasks.first() + tasks
        )
    }

    override suspend fun update(vararg tasks: DailyChallengeTaskEntity) {
        updateAll(tasks.toList())
    }

    override suspend fun updateAll(tasks: List<DailyChallengeTaskEntity>) {
        savedTasks.update { currentTasks ->
            currentTasks.toMutableList().apply {
                tasks.forEach { task ->
                    val index = indexOfFirst { it.id == task.id }
                    if (index != -1) {
                        set(index, task)
                    }
                }
            }
        }
    }

    override suspend fun deleteAll() {
        savedTasks.update { emptyList() }
    }
}
