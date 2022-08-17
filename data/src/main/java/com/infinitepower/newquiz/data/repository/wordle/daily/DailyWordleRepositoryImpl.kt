package com.infinitepower.newquiz.data.repository.wordle.daily

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleDao
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyItem
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyWordleRepositoryImpl @Inject constructor(
    private val dailyWordleDao: DailyWordleDao
) : DailyWordleRepository {
    override suspend fun getAllCalendarItems(): List<WordleDailyCalendarItem> =
        dailyWordleDao.getAllCalendarItems()

    override fun getCalendarItems(
        wordSize: Int,
        month: Month,
        year: Int
    ): FlowResource<List<WordleDailyCalendarItem>> = flow {
        try {
            emit(Resource.Loading())

            val wordSizeItems = dailyWordleDao.getCalendarItemsFlow(wordSize)

            val itemsFilteredFlow = wordSizeItems.map { items ->
                val itemsFiltered = items.filter { item ->
                    item.date.month == month && item.date.year == year
                }

                Resource.Success(itemsFiltered)
            }

            emitAll(itemsFilteredFlow)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading calendar items"))
        }
    }

    override suspend fun insertCalendarItem(item: WordleDailyCalendarItem) {
        dailyWordleDao.insertCalendarItem(item)
    }

    override suspend fun clearAllCalendarItems() {
        dailyWordleDao.clearAllCalendarItems()
    }

    override fun getAllDailyWords(
        wordSize: Int,
        month: Month,
        year: Int
    ): FlowResource<List<WordleDailyItem>> = flow {
        try {
            emit(Resource.Loading())

            val remoteConfig = Firebase.remoteConfig

            val itemsStr = remoteConfig.getString("wordle_daily_words")
            val items: List<WordleDailyItem> = Json.decodeFromString(itemsStr)

            val words = items.filter { item ->
                val itemDate = item.date.toLocalDate()
                itemDate.month == month && itemDate.year == year
            }

            emit(Resource.Success(words))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading daily item"))
        }
    }

    override fun getDailyWord(
        date: LocalDate,
        wordSize: Int
    ): FlowResource<String> = flow {
        try {
            emit(Resource.Loading())

            val remoteConfig = Firebase.remoteConfig

            val itemsStr = remoteConfig.getString("wordle_daily_words")
            val items: List<WordleDailyItem> = Json.decodeFromString(itemsStr)

            val words = items.find { item ->
                item.date.toLocalDate() == date
            }?.words ?: throw NullPointerException("Daily item does not exist")

            val word = words[wordSize.toString()] ?: throw NullPointerException("Word does not exist")

            emit(Resource.Success(word))
        } catch (e: NullPointerException) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Daily item does not exist"))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading daily item"))
        }
    }

    override fun getAvailableDailyWords(): FlowResource<Int> = flow {
        try {
            emit(Resource.Loading())
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading calendar items"))
        }
    }
}