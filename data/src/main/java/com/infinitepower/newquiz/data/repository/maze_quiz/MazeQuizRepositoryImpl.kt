package com.infinitepower.newquiz.data.repository.maze_quiz

import com.infinitepower.newquiz.core.database.dao.MazeQuizDao
import com.infinitepower.newquiz.core.database.model.MazeQuizItemEntity
import com.infinitepower.newquiz.core.database.model.toEntity
import com.infinitepower.newquiz.core.database.model.toMazeQuizItem
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.maze.MazeQuiz
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MazeQuizRepositoryImpl @Inject constructor(
    private val mazeQuizDao: MazeQuizDao
) : MazeQuizRepository {
    override fun getSavedMazeQuizFlow(): FlowResource<MazeQuiz> = flow {
        try {
            emit(Resource.Loading())

            val mazeFlow = mazeQuizDao
                .getAllMazeItemsFlow()
                .map { entities -> entities.map(MazeQuizItemEntity::toMazeQuizItem) }
                .map { mazeItems ->
                    val maze = MazeQuiz(items = mazeItems)
                    Resource.Success(maze)
                }

            emitAll(mazeFlow)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading saved maze quiz"))
        }
    }

    override suspend fun countAllItems(): Int = mazeQuizDao.countAllItems()

    override suspend fun insertItems(items: List<MazeQuiz.MazeItem>) {
        val entities = items.map(MazeQuiz.MazeItem::toEntity)
        mazeQuizDao.insertItems(entities)
    }
}