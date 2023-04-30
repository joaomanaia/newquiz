package com.infinitepower.newquiz

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.infinitepower.newquiz.core.workers.AppStartLoggingAnalyticsWorker
import com.infinitepower.newquiz.data.worker.daily_challenge.VerifyDailyChallengeWorker
import com.infinitepower.newquiz.online_services.core.worker.CheckUserDBWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NewQuizApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    @Inject lateinit var workManager: WorkManager

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(android.util.Log.INFO)
        .build()

    override fun onCreate() {
        super.onCreate()

        initializeRemoteConfig()
        createWorkers()

        Firebase.initialize(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }

    private fun initializeRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig

        val remoteConfigMinFetchInterval = if (BuildConfig.DEBUG) 0L else 3600L
        val remoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = remoteConfigMinFetchInterval
        }
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
    }

    private fun createWorkers() {
        VerifyDailyChallengeWorker.enqueueUniquePeriodicWork(workManager)

        val appStartLoggingAnalyticsWorker = OneTimeWorkRequestBuilder<AppStartLoggingAnalyticsWorker>().build()
        val checkUserDBWorker = OneTimeWorkRequestBuilder<CheckUserDBWorker>().build()

        workManager.enqueue(appStartLoggingAnalyticsWorker)
        workManager.enqueue(checkUserDBWorker)
    }
}