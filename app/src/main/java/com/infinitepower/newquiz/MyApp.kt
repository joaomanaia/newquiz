package com.infinitepower.newquiz

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)
        val testDeviceIds = listOf("8186ECF7C0442AF497706204E55157FF")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}