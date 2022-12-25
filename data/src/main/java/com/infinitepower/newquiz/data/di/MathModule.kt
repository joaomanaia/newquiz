package com.infinitepower.newquiz.data.di

import com.infinitepower.newquiz.core.math.evaluator.Expressions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MathModule {
    @Provides
    @Singleton
    fun provideExpressions(): Expressions = Expressions()
}