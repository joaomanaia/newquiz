package com.infinitepower.newquiz.initializer

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.remote_config.initializer.RemoteConfigInitializer
import com.infinitepower.newquiz.core.workers.AppStartLoggingAnalyticsWorker
import com.infinitepower.newquiz.data.worker.daily_challenge.VerifyDailyChallengeWorker

@Suppress("unused")
class EnqueueStartWorksInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val workManager = WorkManager.getInstance(context)

        VerifyDailyChallengeWorker.enqueueUniquePeriodicWork(workManager)
        AppStartLoggingAnalyticsWorker.enqueue(workManager)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        WorkManagerInitializer::class.java,
        RemoteConfigInitializer::class.java
    )
}