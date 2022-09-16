package com.infinitepower.newquiz.translation_dynamic_feature.di

import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtil
import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TranslatorModule {
    @Binds
    abstract fun bindTranslatorUtil(translatorUtilImpl: TranslatorUtilImpl): TranslatorUtil
}