package com.infinitepower.newquiz.core.testing.domain

import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.database.model.user.ComparisonQuizGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.WordleGameResultEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class FakeGameResultDao : GameResultDao {
    private val multiChoiceResults = MutableStateFlow<List<MultiChoiceGameResultEntity>>(emptyList())

    override suspend fun insertMultiChoiceResult(vararg result: MultiChoiceGameResultEntity) {
        multiChoiceResults.update { currentList ->
            currentList.toMutableList().apply {
                addAll(result.map { it.copy(gameId = Random.nextInt()) })
            }
        }
    }

    override suspend fun getMultiChoiceResults(): List<MultiChoiceGameResultEntity> = multiChoiceResults.first()

    private val wordleResults = MutableStateFlow<List<WordleGameResultEntity>>(emptyList())

    override suspend fun insertWordleResult(vararg result: WordleGameResultEntity) {
        wordleResults.update { currentList ->
            currentList.toMutableList().apply {
                addAll(result.map { it.copy(gameId = Random.nextInt()) })
            }
        }
    }

    override suspend fun getWordleResults(): List<WordleGameResultEntity> = wordleResults.first()

    private val comparisonQuizResults = MutableStateFlow<List<ComparisonQuizGameResultEntity>>(emptyList())

    override suspend fun insertComparisonQuizResult(vararg result: ComparisonQuizGameResultEntity) {
        comparisonQuizResults.update { currentList ->
            currentList.toMutableList().apply {
                addAll(result.map { it.copy(gameId = Random.nextInt()) })
            }
        }
    }

    override suspend fun getComparisonQuizResults(): List<ComparisonQuizGameResultEntity> = comparisonQuizResults.first()

    override suspend fun getXpForDateRange(
        startDate: Long,
        endDate: Long
    ): List<GameResultDao.XpForPlayedAt> {
        return (multiChoiceResults.first() + wordleResults.first() + comparisonQuizResults.first())
            .filter { it.playedAt in startDate..endDate }
            .map {
                GameResultDao.XpForPlayedAt(
                    earnedXp = it.earnedXp,
                    playedAt = it.playedAt
                )
            }
    }

    override fun getXpForDateRangeFlow(
        startDate: Long,
        endDate: Long
    ): Flow<List<GameResultDao.XpForPlayedAt>> = combine(
        multiChoiceResults,
        wordleResults,
        comparisonQuizResults
    ) { multiChoiceResults, wordleResults, comparisonQuizResults ->
        (multiChoiceResults + wordleResults + comparisonQuizResults)
            .filter { it.playedAt in startDate..endDate }
            .map {
                GameResultDao.XpForPlayedAt(
                    earnedXp = it.earnedXp,
                    playedAt = it.playedAt
                )
            }
    }
}