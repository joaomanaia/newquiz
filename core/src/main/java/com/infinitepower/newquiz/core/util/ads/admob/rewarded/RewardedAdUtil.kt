package com.infinitepower.newquiz.core.util.ads.admob.rewarded

import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem

interface RewardedAdUtil {
    fun loadAd(
        adId: String,
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onAdLoaded: () -> Unit = {}
    )

    fun show(
        onUserEarnedReward: (rewardItem: RewardItem) -> Unit = {}
    )

    fun loadAndShow(
        adId: String,
        onAdFailedToLoad: (adError: LoadAdError) -> Unit = {},
        onUserEarnedReward: (rewardItem: RewardItem) -> Unit = {}
    )
}