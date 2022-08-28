package com.infinitepower.newquiz.core.di

import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.MultiChoiceQuizLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.MultiChoiceQuizLoggingAnalyticsImpl
import com.infinitepower.newquiz.core.analytics.logging.wordle.WordleLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.wordle.WordleLoggingAnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggingAnalyticsModule {
    @Binds
    abstract fun bindWordleLoggingAnalytics(
        wordleLoggingAnalyticsImpl: WordleLoggingAnalyticsImpl
    ): WordleLoggingAnalytics

    @Binds
    abstract fun bindMultiChoiceLoggingAnalytics(
        multiChoiceQuizLoggingAnalyticsImpl: MultiChoiceQuizLoggingAnalyticsImpl
    ): MultiChoiceQuizLoggingAnalytics
}