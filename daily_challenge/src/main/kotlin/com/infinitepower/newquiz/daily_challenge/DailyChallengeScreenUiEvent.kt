package com.infinitepower.newquiz.daily_challenge

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.global_event.GameEvent

interface DailyChallengeScreenUiEvent {
    @Keep
    data class OnClaimTaskClick(
        val taskType: GameEvent
    ) : DailyChallengeScreenUiEvent
}