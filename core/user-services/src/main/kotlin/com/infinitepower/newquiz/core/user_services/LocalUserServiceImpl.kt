package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.database.model.user.ComparisonQuizGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.WordleGameResultEntity
import com.infinitepower.newquiz.core.datastore.common.LocalUserCommon
import com.infinitepower.newquiz.core.datastore.di.LocalUserDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.user_services.domain.xp.ComparisonQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.WordleXpGenerator
import com.infinitepower.newquiz.core.user_services.model.User
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalUserServiceImpl @Inject constructor(
    @LocalUserDataStoreManager private val dataStoreManager: DataStoreManager,
    private val remoteConfig: RemoteConfig,
    private val gameResultDao: GameResultDao,
    private val multiChoiceXpGenerator: MultiChoiceQuizXpGenerator,
    private val wordleXpGenerator: WordleXpGenerator,
    private val comparisonQuizXpGenerator: ComparisonQuizXpGenerator
) : LocalUserService {
    override suspend fun userAvailable(): Boolean {
        val uid = dataStoreManager.getPreference(LocalUserCommon.UserUid)
        return uid.isNotBlank()
    }

    override suspend fun getUser(): User? {
        val uid = dataStoreManager.getPreference(LocalUserCommon.UserUid)
        if (uid.isBlank()) return null

        val totalXp = dataStoreManager.getPreference(LocalUserCommon.UserTotalXp)

        val diamonds = getUserDiamonds()

        return User(
            uid = uid,
            totalXp = totalXp.toULong(),
            diamonds = diamonds
        )
    }

    override suspend fun getXpEarnedBy(
        start: Instant,
        end: Instant
    ): XpEarnedByDateTime {
        val xpForDateRange = gameResultDao.getXpForDateRange(
            startDate = start.toEpochMilliseconds(),
            endDate = end.toEpochMilliseconds()
        )

        val tz = TimeZone.currentSystemDefault()

        return xpForDateRange.groupBy {
            Instant.fromEpochMilliseconds(it.playedAt).toLocalDateTime(tz).date.toEpochDays()
        }.mapValues { (_, xpForDay) ->
            xpForDay.sumOf { it.earnedXp }
        }.toSortedMap()
    }

    override suspend fun getXpEarnedBy(timeRange: TimeRange): XpEarnedByDateTime {
        val (start, end) = timeRange.getTimeRange()

        val xpForDateRange = gameResultDao.getXpForDateRange(
            startDate = start.toEpochMilliseconds(),
            endDate = end.toEpochMilliseconds()
        )

        return timeRange.aggregateResults(xpForDateRange)
    }

    override fun getXpEarnedByFlow(timeRange: TimeRange): Flow<XpEarnedByDateTime> {
        val (start, end) = timeRange.getTimeRange()

        return gameResultDao.getXpForDateRangeFlow(
            startDate = start.toEpochMilliseconds(),
            endDate = end.toEpochMilliseconds()
        ).map { xpForDateRange ->
            timeRange.aggregateResults(xpForDateRange)
        }
    }

    override suspend fun getUserDiamonds(): UInt {
        val initialDiamonds = remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS)
        val diamonds = dataStoreManager.getPreference(LocalUserCommon.UserDiamonds(initialDiamonds))

        return diamonds.toUInt()
    }

    override fun getUserDiamondsFlow(): Flow<UInt> {
        val initialDiamonds = remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS)

        return dataStoreManager
            .getPreferenceFlow(LocalUserCommon.UserDiamonds(initialDiamonds))
            .map(Int::toUInt)
    }

    override suspend fun addRemoveDiamonds(diamonds: Int) {
        val initialDiamonds = remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS)
        val currentDiamonds =
            dataStoreManager.getPreference(LocalUserCommon.UserDiamonds(initialDiamonds))
        val newDiamonds = currentDiamonds + diamonds

        dataStoreManager.editPreference(
            key = LocalUserCommon.UserDiamonds(initialDiamonds).key,
            newValue = newDiamonds
        )
    }

    suspend fun updateNewLevelDiamonds() {
        val newLevelDiamonds = remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS)

        addRemoveDiamonds(newLevelDiamonds)
    }

    private fun List<MultiChoiceQuestionStep.Completed>.getAverageQuizTime(): Double {
        return map(MultiChoiceQuestionStep.Completed::questionTime).average()
    }

    private suspend fun saveNewXP(newXp: UInt) {
        val currentUser = getUser()
        checkNotNull(currentUser) { "User not found" }

        val newTotalXp = currentUser.totalXp + newXp

        // Save the new total xp
        dataStoreManager.editPreference(
            key = LocalUserCommon.UserTotalXp.key,
            newValue = newTotalXp.toLong()
        )

        // Check if the user is in a new level
        val isNewLevel = currentUser.isNewLevel(newXp = newXp.toULong())

        // If is new level, update the user diamonds
        if (isNewLevel) {
            updateNewLevelDiamonds()
        }
    }

    override suspend fun saveMultiChoiceGame(
        questionSteps: List<MultiChoiceQuestionStep.Completed>,
        generateXp: Boolean
    ) {
        var newXp = 0u

        // Generate xp if needed
        if (generateXp) {
            // Generate and get the new xp
            newXp = multiChoiceXpGenerator.generateXp(questionSteps)
            saveNewXP(newXp)
        }

        // Save the game result
        val correctAnswers = questionSteps.count { it.correct }
        val skippedQuestions = questionSteps.count { it.skipped }
        val averageAnswerTime = questionSteps.getAverageQuizTime()

        gameResultDao.insertMultiChoiceResult(
            MultiChoiceGameResultEntity(
                correctAnswers = correctAnswers,
                questionCount = questionSteps.size,
                skippedQuestions = skippedQuestions,
                averageAnswerTime = averageAnswerTime,
                earnedXp = newXp.toInt(),
                playedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun saveWordleGame(
        wordLength: UInt,
        rowsUsed: UInt,
        maxRows: Int,
        categoryId: String,
        generateXp: Boolean
    ) {
        var newXp = 0u

        // Generate xp if needed
        if (generateXp) {
            // Generate and get the new xp
            newXp = wordleXpGenerator.generateXp(rowsUsed)
            saveNewXP(newXp)
        }

        // Save the game result
        gameResultDao.insertWordleResult(
            WordleGameResultEntity(
                earnedXp = newXp.toInt(),
                playedAt = System.currentTimeMillis(),
                wordLength = wordLength.toInt(),
                rowsUsed = rowsUsed.toInt(),
                maxRows = maxRows,
                categoryId = categoryId
            )
        )
    }

    override suspend fun saveComparisonQuizGame(
        categoryId: String,
        comparisonMode: String,
        endPosition: UInt,
        skippedAnswers: UInt,
        generateXp: Boolean
    ) {
        var newXp = 0u

        // Generate xp if needed
        if (generateXp) {
            // Generate and get the new xp
            newXp = comparisonQuizXpGenerator.generateXp(
                endPosition = endPosition,
                skippedAnswers = skippedAnswers
            )
            saveNewXP(newXp)
        }

        // Save the game result
        gameResultDao.insertComparisonQuizResult(
            ComparisonQuizGameResultEntity(
                earnedXp = newXp.toInt(),
                playedAt = System.currentTimeMillis(),
                categoryId = categoryId,
                comparisonMode = comparisonMode,
                endPosition = endPosition.toInt(),
                skippedAnswers = skippedAnswers.toInt()
            )
        )
    }
}
