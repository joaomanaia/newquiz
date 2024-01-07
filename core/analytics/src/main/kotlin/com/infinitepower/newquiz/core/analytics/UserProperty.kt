package com.infinitepower.newquiz.core.analytics

import androidx.annotation.Size

sealed class UserProperty(
    @Size(min = 1L, max = 24L) val name: String,
    @Size(max = 36L) val value: String?
) {
    data class WordleLanguage(val lang: String) : UserProperty(
        name = "wordle_lang",
        value = lang
    )

    data class TranslatorModelDownloaded(val downloaded: Boolean) : UserProperty(
        name = "translator_downloaded",
        value = downloaded.toString()
    )
}
