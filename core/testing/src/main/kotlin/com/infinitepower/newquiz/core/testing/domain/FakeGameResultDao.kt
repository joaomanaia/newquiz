package com.infinitepower.newquiz.core.testing.domain

import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.WordleGameResultEntity
import kotlin.random.Random

class FakeGameResultDao : GameResultDao {
    private val multiChoiceResults = mutableListOf<MultiChoiceGameResultEntity>()

    override suspend fun insertMultiChoiceResult(result: MultiChoiceGameResultEntity) {
        multiChoiceResults.add(result.copy(gameId = Random.nextInt()))
    }

    override suspend fun getMultiChoiceResults(): List<MultiChoiceGameResultEntity> = multiChoiceResults

    private val wordleResults = mutableListOf<WordleGameResultEntity>()

    override suspend fun insertWordleResult(result: WordleGameResultEntity) {
        wordleResults.add(result.copy(gameId = Random.nextInt()))
    }

    override suspend fun getWordleResults(): List<WordleGameResultEntity> = wordleResults
}