package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import kotlin.random.Random

class FakeGameResultDao : GameResultDao {
    private val results = mutableListOf<MultiChoiceGameResultEntity>()

    override suspend fun insertMultiChoiceResult(result: MultiChoiceGameResultEntity) {
        results.add(result.copy(gameId = Random.nextInt()))
    }

    override suspend fun getMultiChoiceResults(): List<MultiChoiceGameResultEntity> = results
}