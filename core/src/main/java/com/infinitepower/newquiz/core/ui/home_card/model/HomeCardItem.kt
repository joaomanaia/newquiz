package com.infinitepower.newquiz.core.ui.home_card.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.airbnb.lottie.compose.LottieCompositionSpec

sealed class HomeCardItem {
    fun getId(): Int = this.hashCode()

    data class GroupTitle(
        @StringRes val title: Int
    ) : HomeCardItem()

    data class LargeCard(
        @StringRes val title: Int,
        val icon: CardIcon,
        val enabled: Boolean = true,
        val backgroundPrimary: Boolean = false,
        val onClick: () -> Unit
    ) : HomeCardItem()

    data class MediumCard(
        @StringRes val title: Int,
        val description: String? = null,
        val icon: CardIcon,
        val enabled: Boolean = true,
        val onClick: () -> Unit
    ) : HomeCardItem()

    data class HorizontalItems <out T : Any> (
        val items: List<T>,
        val itemContent: @Composable (item: @UnsafeVariance T) -> Unit
    ) : HomeCardItem()

    data class CustomItem(
        val content: @Composable () -> Unit
    ) : HomeCardItem()
}

sealed class CardIcon {
    data class Icon(val vector: ImageVector) : CardIcon()

    data class Lottie(val spec: LottieCompositionSpec) : CardIcon()
}