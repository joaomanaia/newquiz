package com.infinitepower.newquiz.comparison_quiz.di

import com.infinitepower.newquiz.comparison_quiz.core.ComparisonQuizCoreImpl
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ComparisonQuizModule {
    @Binds
    abstract fun bindComparisonQuizCore(impl: ComparisonQuizCoreImpl): ComparisonQuizCore
}