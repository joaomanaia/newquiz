package com.infinitepower.newquiz.compose.data.local.question

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface SavedQuestionDao {
    @Query("SELECT * FROM saved_questions")
    suspend fun getAllQuestions(): List<Question>

    @Query("SELECT * FROM saved_questions ORDER BY description")
    fun getAllQuestionsOrderByDescriptionPaging(): PagingSource<Int, Question>

    @Query("SELECT * FROM saved_questions ORDER BY type")
    fun getAllQuestionsOrderByTypePaging(): PagingSource<Int, Question>

    @Query("SELECT * FROM saved_questions ORDER BY difficulty")
    fun getAllQuestionsOrderByDifficultyPaging(): PagingSource<Int, Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(vararg question: Question)

    @Delete
    suspend fun deleteQuestions(vararg question: Question)
}