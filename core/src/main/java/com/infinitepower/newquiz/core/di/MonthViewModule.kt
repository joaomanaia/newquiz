package com.infinitepower.newquiz.core.di

import com.infinitepower.newquiz.core.calendar.CalendarMonthViewImpl
import com.infinitepower.newquiz.core.calendar.MonthView
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MonthViewModule {
    @Binds
    abstract fun bindCalendarMonthView(
        calendarMonthViewImpl: CalendarMonthViewImpl
    ): MonthView
}