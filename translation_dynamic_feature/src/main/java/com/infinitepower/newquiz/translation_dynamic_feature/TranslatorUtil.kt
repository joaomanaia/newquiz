package com.infinitepower.newquiz.translation_dynamic_feature

import com.infinitepower.newquiz.core.common.FlowResource

interface TranslatorUtil {
    suspend fun downloadModel(): FlowResource<TranslatorModelState>

    suspend fun deleteModel()

    fun translateAsync(text: String): FlowResource<String>

    suspend fun translate(text: String): String

    suspend fun isModelDownloaded(): Boolean

    sealed interface TranslatorModelState {
        object None : TranslatorModelState

        object Downloaded : TranslatorModelState

        object Downloading : TranslatorModelState
    }
}