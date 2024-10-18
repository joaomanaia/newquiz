package com.infinitepower.newquiz.core.ui

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackbarController {
    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }

    /**
     * Show a short message to the user.
     */
    suspend fun sendShortMessage(message: String) {
        sendEvent(event = SnackbarEvent(message))
    }
}

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration = if (action == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
)

data class SnackbarAction(
    val name: String,
    val action: () -> Unit
)
