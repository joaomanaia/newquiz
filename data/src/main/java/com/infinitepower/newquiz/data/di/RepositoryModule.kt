package com.infinitepower.newquiz.data.di

import com.infinitepower.newquiz.data.repository.question.QuestionRepositoryImpl
import com.infinitepower.newquiz.data.repository.question.saved_questions.SavedQuestionsRepositoryImpl
import com.infinitepower.newquiz.data.repository.wordle.word.WordleRepositoryImpl
import com.infinitepower.newquiz.domain.repository.question.QuestionRepository
import com.infinitepower.newquiz.domain.repository.question.saved_questions.SavedQuestionsRepository
import com.infinitepower.newquiz.domain.repository.wordle.word.WordleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindQuestionRepository(openTDBRepository: QuestionRepositoryImpl): QuestionRepository

    @Binds
    abstract fun bindSavedQuestionsRepository(savedQuestionsRepository: SavedQuestionsRepositoryImpl): SavedQuestionsRepository

    @Binds
    abstract fun bindWordleRepository(wordleRepositoryImpl: WordleRepositoryImpl): WordleRepository
}