package com.infinitepower.newquiz.data.repository.maze_quiz

import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.database.dao.MazeQuizDao
import com.infinitepower.newquiz.core.database.model.MazeQuizItemEntity
import com.infinitepower.newquiz.data.util.mappers.maze.toEntity
import com.infinitepower.newquiz.data.util.mappers.maze.toMazeQuizItem
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.model.maze.MazeQuiz
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MazeQuizRepositoryImpl @Inject constructor(
    private val mazeQuizDao: MazeQuizDao,
    private val analyticsHelper: AnalyticsHelper
) : MazeQuizRepository {
    override fun getSavedMazeQuizFlow(): Flow<MazeQuiz> = mazeQuizDao
        .getAllMazeItemsFlow()
        .map { entities -> entities.map(MazeQuizItemEntity::toMazeQuizItem) }
        .map { mazeItems -> MazeQuiz(items = mazeItems.toPersistentList()) }

    override suspend fun countAllItems(): Int = mazeQuizDao.countAllItems()

    override suspend fun insertItems(items: List<MazeQuiz.MazeItem>) {
        val entities = items.map(MazeQuiz.MazeItem::toEntity)
        mazeQuizDao.insertItems(entities)
    }

    override suspend fun getMazeItemById(id: Int): MazeQuiz.MazeItem? {
        return mazeQuizDao.getMazeItemById(id)?.toMazeQuizItem()
    }

    override suspend fun getNextAvailableMazeItem(): MazeQuiz.MazeItem? {
        return mazeQuizDao.getFirstAvailableMazeItem()?.toMazeQuizItem()
    }

    override suspend fun completeMazeItem(id: Int) {
        val allMazeItems = mazeQuizDao.getAllMazeItems()

        val mazeItem = allMazeItems.find { item ->
            item.id == id
        } ?: throw NullPointerException("Maze item with id $id not found")

        val updatedMazeItem = mazeItem.copy(played = true)

        mazeQuizDao.updateItem(updatedMazeItem)

        // Checks if is maze completed
        val isMazeCompleted = allMazeItems.all { it.played }
        if (isMazeCompleted) analyticsHelper.logEvent(AnalyticsEvent.MazeCompleted(allMazeItems.size))
    }
}
