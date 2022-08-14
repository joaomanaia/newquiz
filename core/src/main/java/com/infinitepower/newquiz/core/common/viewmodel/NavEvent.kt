package com.infinitepower.newquiz.core.common.viewmodel

import com.ramcosta.composedestinations.spec.Direction

sealed class NavEvent {
    object PopBackStack : NavEvent()

    data class Navigate(val direction: Direction) : NavEvent()

    data class ShowSnackBar(val message: String) : NavEvent()
}
