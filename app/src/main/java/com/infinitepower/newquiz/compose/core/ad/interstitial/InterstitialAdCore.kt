package com.infinitepower.newquiz.compose.core.ad.interstitial

interface InterstitialAdCore {
    fun load(adId: String)

    fun show()
}