package com.infinitepower.newquiz.initializer

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.workers.AppStartLoggingAnalyticsWorker
import com.infinitepower.newquiz.data.worker.daily_challenge.VerifyDailyChallengeWorker
import com.infinitepower.newquiz.initializer.firebase.FirebaseRemoteConfigInitializer
import com.infinitepower.newquiz.online_services.core.worker.CheckUserDBWorker

@Suppress("unused")
class EnqueueStartWorksInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val workManager = WorkManager.getInstance(context)

        VerifyDailyChallengeWorker.enqueueUniquePeriodicWork(workManager)
        AppStartLoggingAnalyticsWorker.enqueue(workManager)
        CheckUserDBWorker.enqueue(workManager)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        WorkManagerInitializer::class.java,
        FirebaseRemoteConfigInitializer::class.java
    )
}