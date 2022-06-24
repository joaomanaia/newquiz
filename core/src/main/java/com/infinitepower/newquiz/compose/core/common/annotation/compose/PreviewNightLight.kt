package com.infinitepower.newquiz.compose.core.common.annotation.compose

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class PreviewNightLight
@Preview(
    showBackground = true,
    group = "Portrait",
)
@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=2340,height=1080,unit=px,dpi=440",
    group = "Landscape",
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Portrait",
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:shape=Normal,width=2340,height=1080,unit=px,dpi=440",
    group = "Landscape",
)
annotation class PreviewNightLightPortraitLandscape