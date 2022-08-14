package com.infinitepower.newquiz.core.ui.home_card.model

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.airbnb.lottie.compose.LottieCompositionSpec

sealed class HomeCardItem {
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

    data class MediumCard(
        override val title: Int,
        val description: String? = null,
        val icon: CardIcon,
        val enabled: Boolean = true,
        val onClick: () -> Unit
    ) : HomeCardItem()
}

sealed class CardIcon {
    data class Icon(val vector: ImageVector) : CardIcon()

    data class Lottie(val spec: LottieCompositionSpec) : CardIcon()
}