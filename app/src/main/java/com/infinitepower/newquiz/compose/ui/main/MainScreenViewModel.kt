package com.infinitepower.newquiz.compose.ui.main

import androidx.lifecycle.ViewModel
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    val authUserApi: AuthUserApi
) : ViewModel()