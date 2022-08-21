package com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions

import androidx.room.*
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedMultiChoiceQuestionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<MultiChoiceQuestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(vararg questions: MultiChoiceQuestion)

    @Query("SELECT * FROM saved_multi_choice_questions")
    fun getFlowQuestions(): Flow<List<MultiChoiceQuestion>>

    @Query("SELECT * FROM saved_multi_choice_questions")
    suspend fun getQuestions(): List<MultiChoiceQuestion>

    @Delete
    suspend fun deleteAll(questions: List<MultiChoiceQuestion>)
}