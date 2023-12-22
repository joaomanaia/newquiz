package com.infinitepower.newquiz.feature.settings.screens.general

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo

@Keep
data class GeneralScreenUiState(
    val defaultShowCategoryConnectionInfo: ShowCategoryConnectionInfo = ShowCategoryConnectionInfo.NONE
)
