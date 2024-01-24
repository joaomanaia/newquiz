package com.infinitepower.newquiz.model

@JvmInline
value class Language(val value: String) {
    companion object {
        val ENGLISH = Language("en")
    }

    init {
        require(value.isNotEmpty()) { "Language value must not be empty" }
    }
}
