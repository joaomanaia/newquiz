package com.infinitepower.newquiz.domain.repository.wordle.daily

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyWordleDao {
    @Query("SELECT * FROM wordle_daily_calendar")
    suspend fun getAllCalendarItems(): List<WordleDailyCalendarItem>

    @Query("SELECT * FROM wordle_daily_calendar WHERE wordSize = :wordSize")
    fun getCalendarItemsFlow(
        wordSize: Int
    ): Flow<List<WordleDailyCalendarItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendarItem(item: WordleDailyCalendarItem)

    @Query("DELETE FROM wordle_daily_calendar")
    suspend fun clearAllCalendarItems()
}