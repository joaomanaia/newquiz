package com.infinitepower.newquiz.data.di

import com.infinitepower.newquiz.data.repository.question.QuestionRepositoryImpl
import com.infinitepower.newquiz.domain.repository.question.QuestionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindQuestionRepository(openTDBRepository: QuestionRepositoryImpl): QuestionRepository
}