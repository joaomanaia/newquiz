package com.infinitepower.newquiz.core_test.utils

import androidx.annotation.VisibleForTesting
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.infinitepower.newquiz.core_test.compose.theme.NewQuizTestTheme
import org.jetbrains.annotations.TestOnly
import java.util.Locale

/**
 * Sets the content of the [ComposeContentTestRule] to the given [composable] wrapped in a [NewQuizTestTheme].
 * The [locale] can be used to set the device locale.
 * The [darkTheme] and [dynamicColor] can be used to set the theme.
 * This function is only available in tests.
 *
 * @param locale The locale to set the device to.
 * @param darkTheme Whether to use the dark theme.
 * @param dynamicColor Whether to use dynamic colors.
 * @param composable The composable to set as content.
 */
@TestOnly
@VisibleForTesting
fun ComposeContentTestRule.setTestContent(
    locale: Locale = Locale.ENGLISH,
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    composable: @Composable () -> Unit
) {
    setContent {
        NewQuizTestTheme(
            darkTheme = darkTheme,
            dynamicColor = dynamicColor
        ) {
            setTestDeviceLocale(locale = locale)

            Surface {
                composable()
            }
        }
    }
}
