package com.infinitepower.newquiz.data.di

import com.infinitepower.newquiz.data.repository.multi_choice_quiz.MultiChoiceQuestionRepositoryImpl
import com.infinitepower.newquiz.data.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepositoryImpl
import com.infinitepower.newquiz.data.repository.wordle.WordleRepositoryImpl
import com.infinitepower.newquiz.data.repository.wordle.daily.DailyWordleRepositoryImpl
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMultiChoiceQuestionRepository(openTDBRepository: MultiChoiceQuestionRepositoryImpl): MultiChoiceQuestionRepository

    @Binds
    abstract fun bindSavedMultiChoiceQuestionsRepository(savedQuestionsRepository: SavedMultiChoiceQuestionsRepositoryImpl): SavedMultiChoiceQuestionsRepository

    @Binds
    abstract fun bindWordleRepository(wordleRepositoryImpl: WordleRepositoryImpl): WordleRepository

    @Binds
    abstract fun bindDailyWordleRepository(dailyWordleRepositoryImpl: DailyWordleRepositoryImpl): DailyWordleRepository
}