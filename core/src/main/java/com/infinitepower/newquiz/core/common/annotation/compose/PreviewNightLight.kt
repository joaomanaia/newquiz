package com.infinitepower.newquiz.core.common.annotation.compose

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class PreviewNightLight

@Preview(
    showBackground = true,
    device = "spec:width=673.5dp,height=841dp,dpi=480"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=673.5dp,height=841dp,dpi=480"
)
annotation class PreviewMediumNightLight

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=480"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=1280dp,height=800dp,dpi=480"
)
annotation class PreviewExpandedNightLight