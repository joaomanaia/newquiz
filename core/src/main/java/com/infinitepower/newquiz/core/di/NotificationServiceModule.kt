package com.infinitepower.newquiz.core.di

import com.infinitepower.newquiz.core.notification.wordle.DailyWordleNotificationService
import com.infinitepower.newquiz.core.notification.wordle.DailyWordleNotificationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationServiceModule {
    @Binds
    abstract fun bindDailyWordleNotificationService(
        dailyWordleNotificationServiceImpl: DailyWordleNotificationServiceImpl
    ): DailyWordleNotificationService
}