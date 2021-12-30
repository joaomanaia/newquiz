package com.infinitepower.newquiz.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.infinitepower.newquiz.compose.ui.theme.NewQuizTheme
import com.infinitepower.newquiz.compose.core.navigation.Navigation
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authUserApi: AuthUserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for the content view.
        setContent {
            NewQuizTheme {
                Surface {
                    Navigation(authUserApi = authUserApi)
                }
            }
        }
    }
}