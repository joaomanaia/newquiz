package com.infinitepower.newquiz.compose

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)
        val testDeviceIds = listOf("8186ECF7C0442AF497706204E55157FF")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
    }
}