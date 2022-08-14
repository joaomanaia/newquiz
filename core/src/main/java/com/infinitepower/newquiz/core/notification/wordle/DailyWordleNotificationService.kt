package com.infinitepower.newquiz.core.notification.wordle

import androidx.annotation.WorkerThread

interface DailyWordleNotificationService {
    @WorkerThread
    fun showNotification()
}