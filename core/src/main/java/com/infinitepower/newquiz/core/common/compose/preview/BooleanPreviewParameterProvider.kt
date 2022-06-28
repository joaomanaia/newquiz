package com.infinitepower.newquiz.core.common.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}