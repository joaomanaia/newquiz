package com.infinitepower.newquiz.translation.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.infinitepower.newquiz.translation.TranslatorUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * This worker is responsible for downloading the translation model.
 */
@HiltWorker
class DownloadTranslationModelWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val translatorUtil: TranslatorUtil
) : CoroutineWorker(context, workerParams) {
    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    companion object {
        private const val TRANSLATION_LANGUAGE_KEY = "translation_language_key"
        private const val REQUIRE_WIFI = "require_wifi"
        private const val REQUIRE_CHARGING = "require_charging"

        private const val WORK_TAG = "download_translation_model"

        private const val NOTIFICATION_CHANNEL_ID = "download_translation_model_channel"
        private const val NOTIFICATION_ID = 1

        /**
         * Enqueues an unique work to download the translation model.
         *
         * @param workManager The [WorkManager] instance.
         * @param targetLanguage The language of the translation model.
         * @return The [UUID] of the work.
         */
        fun enqueueWork(
            workManager: WorkManager,
            targetLanguage: String,
            requireWifi: Boolean = true,
            requireCharging: Boolean = false
        ): UUID {
            val requiredNetworkType = if (requireWifi) {
                NetworkType.UNMETERED
            } else {
                NetworkType.CONNECTED
            }

            val workConstraints = Constraints(
                requiredNetworkType = requiredNetworkType,
                requiresCharging = requireCharging,
                requiresStorageNotLow = true
            )

            val workRequest = OneTimeWorkRequestBuilder<DownloadTranslationModelWorker>()
                .addTag(WORK_TAG)
                .setConstraints(workConstraints)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                    workDataOf(
                        TRANSLATION_LANGUAGE_KEY to targetLanguage,
                        REQUIRE_WIFI to requireWifi,
                        REQUIRE_CHARGING to requireCharging
                    )
                ).build()

            workManager.enqueueUniqueWork(WORK_TAG, ExistingWorkPolicy.REPLACE, workRequest)

            return workRequest.id
        }

        /**
         * Gets the [WorkInfo] of the work with the given [workId].
         *
         * @param workManager The [WorkManager] instance.
         * @param workId The [UUID] of the work.
         * @return The [WorkInfo] of the work.
         */
        fun getWorkInfo(
            workManager: WorkManager,
            workId: UUID
        ): Flow<WorkInfo> {
            return workManager.getWorkInfoByIdLiveData(workId).asFlow()
        }
    }

    override suspend fun doWork(): Result {
        // Get the translation language
        val translationLanguage = inputData.getString(TRANSLATION_LANGUAGE_KEY)
            ?: return Result.failure()

        val requireWifi = inputData.getBoolean(REQUIRE_WIFI, true)
        val requireCharging = inputData.getBoolean(REQUIRE_CHARGING, false)

        // Check if the target language is supported
        val languages = translatorUtil.availableTargetLanguageCodes
        if (!languages.contains(translationLanguage)) throw RuntimeException("Language not supported")

        val progress = "Downloading $translationLanguage model"
        setForeground(createForegroundInfo(progress))

        translatorUtil.downloadModel(
            targetLanguage = translationLanguage,
            requireWifi = requireWifi,
            requireCharging = requireCharging
        )

        return Result.success()
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val title = "Downloading translation model"
        val cancel = "Cancel"
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)

        // Create the notification channel if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .setProgress(0, 0, true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setSilent(true)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)

        /*
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }

         */
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Download translation model",
            NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
    }
}