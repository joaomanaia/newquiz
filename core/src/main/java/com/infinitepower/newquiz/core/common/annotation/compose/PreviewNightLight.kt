package com.infinitepower.newquiz.core.common.annotation.compose

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@PreviewNightLight
@PreviewMediumNightLight
@PreviewExpandedNightLight
annotation class AllPreviewsNightLight

@Preview(
    showBackground = true,
    group = "Compact"
)
@Preview(
    showBackground = true,
    group = "Compact", device = "spec:parent=pixel_5,orientation=landscape"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Compact"
)
annotation class PreviewNightLight

@Preview(
    showBackground = true,
    device = "spec:width=673.5dp,height=841dp,dpi=480",
    group = "Medium"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=673.5dp,height=841dp,dpi=480",
    group = "Medium"
)
annotation class PreviewMediumNightLight

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=480",
    group = "Expanded"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=1280dp,height=800dp,dpi=480",
    group = "Expanded"
)
annotation class PreviewExpandedNightLight