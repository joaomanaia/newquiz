package com.infinitepower.newquiz.core.ui.text

import android.icu.text.CompactDecimalFormat
import android.os.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow

@Composable
fun CompactDecimalText(
    modifier: Modifier = Modifier,
    value: Long,
    style: TextStyle = LocalTextStyle.current
) {
    val text = remember(value) { getCompactDecimalText(value) }

    Text(
        modifier = modifier,
        text = text,
        style = style
    )
}

fun getCompactDecimalText(
    value: Long
): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val formatter = CompactDecimalFormat.getInstance(
            Locale.getDefault(),
            CompactDecimalFormat.CompactStyle.SHORT
        )

        formatter.format(value)
    } else {
        value.compactFormat()
    }
}

private fun Long.compactFormat(): String {
    if (this < 1000) return toString()
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f %c", this / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
}

@Composable
@PreviewNightLight
fun CompactDecimalTextPreview() {
    NewQuizTheme {
        Surface {
            CompactDecimalText(
                modifier = Modifier.padding(16.dp),
                value = 1234
            )
        }
    }
}