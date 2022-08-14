package com.infinitepower.newquiz.core.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class NavEventViewModel : ViewModel() {
    private val _navEvent = MutableSharedFlow<NavEvent>()
    val navEvent = _navEvent.asSharedFlow()

    fun sendUiEvent(uiEvent: NavEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _navEvent.emit(uiEvent)
        }
    }
}