package com.infinitepower.newquiz.core.ui.ads.admob

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlin.math.roundToInt

@Composable
fun BannerAd(
    modifier: Modifier = Modifier,
    inEditMode: Boolean = LocalInspectionMode.current,
    adId: String?,
    adSize: AdSize? = null
) {
    if (inEditMode || adId == null) {
        Surface(
            tonalElevation = 8.dp,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Banner ad",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
    } else {
        BoxWithConstraints(modifier = modifier) {
            BannerAdImpl(
                modifier = Modifier,
                adId = adId,
                adSize = adSize ?: getAdaptiveAdSize()
            )
        }
    }
}

@Composable
private fun BannerAdImpl(
    modifier: Modifier = Modifier,
    adId: String,
    adSize: AdSize,
    //consentInformation: ConsentInformation? = null
) {
    val adRequest = remember {
        // npa -> non-personalized ads
        //val npa = consentInformation?.consentStatus == ConsentInformation.ConsentStatus.NOT_REQUIRED
        //val extras = bundleOf("npa" to npa.toInt())

        AdRequest
            .Builder()
            //.addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId = adId
                loadAd(adRequest)
            }
        }
    )
}

@Composable
@ReadOnlyComposable
fun BoxWithConstraintsScope.getAdaptiveAdSize(): AdSize {
    val context = LocalContext.current
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, maxWidth.value.roundToInt())
}