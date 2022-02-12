package com.infinitepower.newquiz.compose

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.infinitepower.newquiz.compose.core.firebase.EmulatorSuite
import dagger.hilt.android.HiltAndroidApp
import leakcanary.AppWatcher
import leakcanary.ReachabilityWatcher
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    @Inject lateinit var emulatorSuite: EmulatorSuite

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)
        val testDeviceIds = listOf("8186ECF7C0442AF497706204E55157FF")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        //emulatorSuite.enableAll()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}