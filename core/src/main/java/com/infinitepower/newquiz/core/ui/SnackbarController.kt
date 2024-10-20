package com.infinitepower.newquiz.core.ui

import androidx.compose.material3.SnackbarDuration
import com.infinitepower.newquiz.model.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackbarController {
    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }

    suspend fun sendShortMessage(message: UiText) {
        sendEvent(event = SnackbarEvent(message))
    }

    /**
     * Show a short message to the user.
     */
    suspend fun sendShortMessage(message: String) {
        sendEvent(event = SnackbarEvent(UiText.DynamicString(message)))
    }
}

data class SnackbarEvent(
    val message: UiText,
    val action: SnackbarAction? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration = if (action == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
)

data class SnackbarAction(
    val name: UiText,
    val action: () -> Unit
)
