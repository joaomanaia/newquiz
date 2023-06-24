package com.infinitepower.newquiz.translation_dynamic_feature

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtil.TranslatorModelState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslatorUtilImpl @Inject constructor() : TranslatorUtil {
    private val localeLanguage by lazy {
        Locale.getDefault().language
    }

    private val translator: Translator by lazy {
        val options = TranslatorOptions
            .Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(localeLanguage)
            .build()

        Translation.getClient(options)
    }

    private val manager by lazy { RemoteModelManager.getInstance() }

    override suspend fun downloadModel(): FlowResource<TranslatorModelState> = flow {
        emit(com.infinitepower.newquiz.model.Resource.Loading(TranslatorModelState.Downloading))

        val conditions = DownloadConditions
            .Builder()
            .requireWifi()
            .build()

        try {
            translator
                .downloadModelIfNeeded(conditions)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                com.infinitepower.newquiz.model.Resource.Error(
                    message = e.localizedMessage ?: "Error while downloading translation model",
                    data = TranslatorModelState.None
                )
            )
        } finally {
            emit(com.infinitepower.newquiz.model.Resource.Success(TranslatorModelState.Downloaded))
        }
    }

    override suspend fun deleteModel() {
        val localeModel = TranslateRemoteModel
            .Builder(localeLanguage)
            .build()

        manager.deleteDownloadedModel(localeModel).await()
    }

    override fun translateAsync(text: String): FlowResource<String> = flow {
        try {
            emit(com.infinitepower.newquiz.model.Resource.Loading())
            if (!isModelDownloaded()) throw RuntimeException("No model downloaded")

            val translatedText = translate(text)
            emit(com.infinitepower.newquiz.model.Resource.Success(translatedText))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(com.infinitepower.newquiz.model.Resource.Error(e.localizedMessage ?: "Error while translating text"))
        }
    }

    override suspend fun translate(text: String): String {
        return translator.translate(text).await()
    }

    override suspend fun isModelDownloaded(): Boolean {
        val localeModel = TranslateRemoteModel
            .Builder(localeLanguage)
            .build()

        return manager.isModelDownloaded(localeModel).await()
    }
}