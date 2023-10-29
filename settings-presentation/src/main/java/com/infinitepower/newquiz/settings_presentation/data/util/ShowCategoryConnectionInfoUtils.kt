package com.infinitepower.newquiz.settings_presentation.data.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ReadOnlyComposable
internal fun ShowCategoryConnectionInfo.getTitle(): UiText {
    return when (this) {
        ShowCategoryConnectionInfo.NONE -> UiText.StringResource(CoreR.string.none)
        ShowCategoryConnectionInfo.BOTH -> UiText.StringResource(CoreR.string.both)
        ShowCategoryConnectionInfo.REQUIRE_CONNECTION -> UiText.StringResource(CoreR.string.require_internet_connection)
        ShowCategoryConnectionInfo.DONT_REQUIRE_CONNECTION -> UiText.StringResource(CoreR.string.dont_require_internet_connection)
    }
}

@Composable
@ReadOnlyComposable
internal fun getShowCategoryConnectionInfoEntryMap(): Map<String, String> {
    return ShowCategoryConnectionInfo.entries.associate { info ->
        info.name to info.getTitle().asString()
    }
}