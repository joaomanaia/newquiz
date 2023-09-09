package com.infinitepower.newquiz.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinitepower.newquiz.core.database.model.MultiChoiceQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedMultiChoiceQuestionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<MultiChoiceQuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(vararg questions: MultiChoiceQuestionEntity)

    @Query("SELECT * FROM saved_multi_choice_questions")
    fun getFlowQuestions(): Flow<List<MultiChoiceQuestionEntity>>

    @Query("SELECT * FROM saved_multi_choice_questions ORDER BY description ASC")
    fun getFlowQuestionsSortedByDescription(): Flow<List<MultiChoiceQuestionEntity>>

    @Query("SELECT * FROM saved_multi_choice_questions ORDER BY category ASC")
    fun getFlowQuestionsSortedByCategory(): Flow<List<MultiChoiceQuestionEntity>>

    @Query("SELECT * FROM saved_multi_choice_questions")
    suspend fun getQuestions(): List<MultiChoiceQuestionEntity>

    @Delete
    suspend fun deleteAll(questions: List<MultiChoiceQuestionEntity>)
}