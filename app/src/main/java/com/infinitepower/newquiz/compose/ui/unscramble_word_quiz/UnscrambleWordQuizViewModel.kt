package com.infinitepower.newquiz.compose.ui.unscramble_word_quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.compose.core.word_scrambler.WordScrambler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class UnscrambleWordQuizViewModel @Inject constructor(
    private val wordScrambler: WordScrambler
) : ViewModel() {
    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _currentWordCount = MutableStateFlow(0)
    val currentWordCount = _currentWordCount.asStateFlow()

    private val _currentScrambledWord = MutableStateFlow("")

    val currentScrambledWord = _currentScrambledWord.flatMapLatest { word ->
        flow {
            while (true) {
                emit(wordScrambler.scrambleWord(word))
                delay(5000)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _currentScrambledWord.emit(Faker().color.name())
        }
    }

    private val _count = MutableStateFlow(0)
    val count = _count.asStateFlow()
}