package com.infinitepower.newquiz.compose.di

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.infinitepower.newquiz.compose.core.ad.interstitial.InterstitialAdCore
import com.infinitepower.newquiz.compose.core.ad.interstitial.InterstitialAdCoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    @Provides
    fun provideInterstitialAdCore(
        adRequest: AdRequest,
        activity: Activity
    ): InterstitialAdCore = InterstitialAdCoreImpl(adRequest, activity)
}