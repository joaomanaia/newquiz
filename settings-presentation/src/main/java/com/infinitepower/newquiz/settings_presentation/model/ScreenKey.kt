package com.infinitepower.newquiz.settings_presentation.model

@JvmInline
value class ScreenKey(val value: String) {
    init {
        require(value.isNotEmpty()) { "key must not be empty" }
    }
}