package com.infinitepower.newquiz.domain.repository.question.saved_questions

import androidx.room.*
import com.infinitepower.newquiz.model.question.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedQuestionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(vararg questions: Question)

    @Query("SELECT * FROM saved_questions")
    fun getFlowQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM saved_questions")
    suspend fun getQuestions(): List<Question>

    @Delete
    suspend fun deleteAll(questions: List<Question>)
}