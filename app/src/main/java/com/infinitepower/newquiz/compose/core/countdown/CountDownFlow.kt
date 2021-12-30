package com.infinitepower.newquiz.compose.core.countdown

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class CountDownFlow(
    private val startValueMills: Long,
    private val countDownInterval: Long
) {
    private val currentStartValue = MutableStateFlow(0L)

    private val isCounting = MutableStateFlow(false)

    val countDownFlow = isCounting.flatMapLatest { counting ->
        flow {
            var currentValue = currentStartValue.first()
            emit(currentValue)

            while (counting && currentValue > 0) {
                delay(countDownInterval)
                currentValue -= countDownInterval
                emit(currentValue)
                if (currentValue == 0) onFinish.emit(true)
            }
        }
    }

    val onFinish = MutableSharedFlow<Boolean>()

    suspend fun start() {
        currentStartValue.emit(startValueMills)
        isCounting.emit(true)
    }

    suspend fun pause() {
        isCounting.emit(false)
    }

    suspend fun reset() {
        isCounting.emit(false)
        countDownFlow.retry()
    }
}