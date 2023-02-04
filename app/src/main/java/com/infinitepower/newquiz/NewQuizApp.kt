package com.infinitepower.newquiz

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
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
import com.infinitepower.newquiz.core.notification.wordle.DailyWordleNotificationServiceImpl
import com.infinitepower.newquiz.core.workers.AppStartLoggingAnalyticsWorker
import com.infinitepower.newquiz.online_services.core.worker.CheckUserDBWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

private const val TWELVE_HOUR_IN_SECONDS = 3600L

@HiltAndroidApp
class NewQuizApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val workManager by lazy {
        WorkManager.getInstance(this)
    }

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

        val remoteConfigMinFetchInterval = 0L //if (BuildConfig.DEBUG) 0L else TWELVE_HOUR_IN_SECONDS
        val remoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = remoteConfigMinFetchInterval
        }
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels() {
        val channels = listOf(
            createNotificationChannel(
                id = DailyWordleNotificationServiceImpl.DAILY_WORDLE_CHANNEL_ID,
                name = "Daily Wordle",
                description = "New daily word notification",
                importance = NotificationManager.IMPORTANCE_LOW
            )
        )

        NotificationManagerCompat.from(this).createNotificationChannels(channels)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        id: String,
        name: String,
        description: String?,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ): NotificationChannel {
        val channel = NotificationChannel(
            id,
            name,
            importance
        )
        channel.description = description

        return channel
    }

    private fun createWorkers() {
        val appStartLoggingAnalyticsWorker = OneTimeWorkRequestBuilder<AppStartLoggingAnalyticsWorker>().build()
        val checkUserDBWorker = OneTimeWorkRequestBuilder<CheckUserDBWorker>().build()

        workManager
            .beginWith(appStartLoggingAnalyticsWorker)
            .then(checkUserDBWorker)
            .enqueue()
    }
}