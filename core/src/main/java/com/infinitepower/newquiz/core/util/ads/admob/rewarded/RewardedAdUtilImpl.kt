package com.infinitepower.newquiz.core.util.ads.admob.rewarded

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import javax.inject.Inject

class RewardedAdUtilImpl @Inject constructor(
    private val activity: Activity
) : RewardedAdUtil {
    companion object {
        private const val TAG = "RewardAdUtil"
    }

    private val adRequest: AdRequest by lazy {
        AdRequest.Builder().build()
    }

    private var mRewardedAd: RewardedAd? = null

    override fun loadAd(
        adId: String,
        onAdFailedToLoad: (adError: LoadAdError) -> Unit,
        onAdLoaded: () -> Unit
    ) {
        RewardedAd.load(activity, adId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mRewardedAd = null
                Log.e(TAG, adError.message)
                onAdFailedToLoad(adError)
            }

            override fun onAdLoaded(ad: RewardedAd) {
                mRewardedAd = ad
                onAdLoaded()
            }
        })
    }

    override fun show(
        onUserEarnedReward: (rewardItem: RewardItem) -> Unit
    ) {
        if (mRewardedAd != null) {
            mRewardedAd?.show(activity) { item ->
                val rewardAmount = item.amount
                val rewardType = item.type

                onUserEarnedReward(item)
                Log.d(TAG, "User earned the reward.\nReward amount: $rewardAmount\nReward type: $rewardType")
            }
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }

    override fun loadAndShow(
        adId: String,
        onAdFailedToLoad: (adError: LoadAdError) -> Unit,
        onUserEarnedReward: (rewardItem: RewardItem) -> Unit
    ) {
        loadAd(
            adId = adId,
            onAdFailedToLoad = onAdFailedToLoad,
            onAdLoaded = { show(onUserEarnedReward) }
        )
    }
}