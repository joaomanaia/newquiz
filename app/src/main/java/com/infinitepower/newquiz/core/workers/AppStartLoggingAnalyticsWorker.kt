package com.infinitepower.newquiz.core.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.analytics.UserProperty
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.translation.TranslatorUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AppStartLoggingAnalyticsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val translatorUtil: TranslatorUtil,
    private val analyticsHelper: AnalyticsHelper
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        fun enqueue(workManager: WorkManager) {
            val appStartLoggingAnalyticsWorker = OneTimeWorkRequestBuilder<AppStartLoggingAnalyticsWorker>().build()
            workManager.enqueue(appStartLoggingAnalyticsWorker)
        }
    }

    override suspend fun doWork(): Result {
        val wordleLang = settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)
        analyticsHelper.setUserProperty(UserProperty.WordleLanguage(wordleLang))

        val isTranslatorModelDownloaded = translatorUtil.isModelDownloaded()
        analyticsHelper.setUserProperty(UserProperty.TranslatorModelDownloaded(isTranslatorModelDownloaded))

        return Result.success()
    }
}
