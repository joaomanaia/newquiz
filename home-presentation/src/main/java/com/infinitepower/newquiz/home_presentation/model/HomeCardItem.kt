package com.infinitepower.newquiz.home_presentation.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.airbnb.lottie.compose.LottieCompositionSpec

internal sealed class HomeCardItem {
    @get:StringRes
    abstract val title: Int

    fun getId(): Int = this.hashCode()

    data class GroupTitle(
        override val title: Int
    ) : HomeCardItem()

    data class LargeCard(
        override val title: Int,
        val icon: CardIcon,
        val enabled: Boolean = true,
        val onClick: () -> Unit
    ) : HomeCardItem()
}

internal sealed class CardIcon {
    data class Icon(val vector: ImageVector) : CardIcon()

    data class Lottie(val spec: LottieCompositionSpec) : CardIcon()
}