package com.infinitepower.newquiz.core.di

import com.infinitepower.newquiz.core.analytics.logging.CoreLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.CoreLoggingAnalyticsImpl
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
    abstract fun binCoreLoggingAnalytics(
        coreLoggingAnalyticsImpl: CoreLoggingAnalyticsImpl
    ): CoreLoggingAnalytics

    @Binds
    abstract fun bindWordleLoggingAnalytics(
        wordleLoggingAnalyticsImpl: WordleLoggingAnalyticsImpl
    ): WordleLoggingAnalytics

    @Binds
    abstract fun bindMultiChoiceLoggingAnalytics(
        multiChoiceQuizLoggingAnalyticsImpl: MultiChoiceQuizLoggingAnalyticsImpl
    ): MultiChoiceQuizLoggingAnalytics
}