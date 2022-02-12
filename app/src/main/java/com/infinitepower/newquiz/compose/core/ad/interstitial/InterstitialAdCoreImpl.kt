package com.infinitepower.newquiz.compose.core.ad.interstitial

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

class InterstitialAdCoreImpl(
    private val adRequest: AdRequest,
    private val activity: Activity
) : InterstitialAdCore {
    companion object {
        private const val TAG = "InterstitialAdCoreImpl"
    }

    private var mInterstitialAd: InterstitialAd? = null

    override fun load(adId: String) {
        InterstitialAd.load(activity.baseContext, adId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

    override fun show() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
            mInterstitialAd = null
        }
    }
}