package com.infinitepower.newquiz.core.notification.wordle

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.infinitepower.newquiz.core.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyWordleNotificationServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DailyWordleNotificationService {
    private val notificationManager by lazy {
        NotificationManagerCompat.from(context)
    }

    @WorkerThread
    override fun showNotification() {
        val intent = Intent(context, Class.forName(MAIN_ACTIVITY_CLASS_NAME))

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat
            .Builder(context, DAILY_WORDLE_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("NewQuiz")
            .setContentText("New daily word is here for you to discover!")
            .setContentIntent(pendingIntent)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        notificationManager.notify(0, notification)
    }

    companion object {
        private const val MAIN_ACTIVITY_CLASS_NAME = "com.infinitepower.newquiz.MainActivity"

        const val DAILY_WORDLE_CHANNEL_ID = "daily_wordle_channel"
    }
}