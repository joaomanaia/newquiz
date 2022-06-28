package com.infinitepower.newquiz.core.navigation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@NavGraph
@RootNavGraph(start = true)
annotation class MainNavGraph(
    val start: Boolean = false
)