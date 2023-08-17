package com.infinitepower.newquiz

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    distribution,
    // translation
}

@Suppress("EnumEntryName")
enum class NewQuizFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null
) {
    normal(FlavorDimension.distribution),
    foss(FlavorDimension.distribution),

    // translation(FlavorDimension.translation),
    // noTranslation(FlavorDimension.translation),
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: NewQuizFlavor) -> Unit = {}
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.values().map { it.name }

        productFlavors {
            NewQuizFlavor.values().forEach { flavor ->
                create(flavor.name) {
                    dimension = flavor.dimension.name
                    flavorConfigurationBlock(this, flavor)

                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (flavor.applicationIdSuffix != null) {
                            applicationIdSuffix = flavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}
