package com.infinitepower.newquiz.core.util.ui.nav_drawer

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
class NavDrawerUtilImpl constructor(
    private val drawerState: DrawerState,
    private val coroutineScope: CoroutineScope
) : NavDrawerUtil {
    override fun open() {
        coroutineScope.launch {
            drawerState.open()
        }
    }

    override fun close() {
        coroutineScope.launch {
            drawerState.close()
        }
    }
}