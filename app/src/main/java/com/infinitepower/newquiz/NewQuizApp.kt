package com.infinitepower.newquiz

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.infinitepower.newquiz.core.notification.wordle.DailyWordleNotificationServiceImpl
import com.infinitepower.newquiz.data.worker.wordle.DailyWordleNotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val ONE_HOUR_IN_SECONDS = 3600L
private const val TWELVE_HOUR_IN_SECONDS = 3600L

private const val WORDLE_NOTIFICATION_HOUR = 8

@HiltAndroidApp
class NewQuizApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        initializeMobileAds()
        initializeRemoteConfig()
        createNotificationChannels()
        createDailyWordleWork()
    }

    private fun initializeMobileAds() {
        MobileAds.initialize(this)
    }

    private fun initializeRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig

        val remoteConfigMinFetchInterval = if (BuildConfig.DEBUG) ONE_HOUR_IN_SECONDS else TWELVE_HOUR_IN_SECONDS
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

    private fun createDailyWordleWork() {
        val request = PeriodicWorkRequestBuilder<DailyWordleNotificationWorker>(
            24,
            TimeUnit.HOURS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
            TimeUnit.MILLISECONDS
        ).setInitialDelay(24, TimeUnit.HOURS).build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "daily_wordle",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}