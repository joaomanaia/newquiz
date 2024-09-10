package com.infinitepower.newquiz.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun DataCollectionConsentDialog(
    onAgreeClick: () -> Unit = {},
    onDisagreeClick: () -> Unit = {}
) {
    AlertDialog(
        title = { Text(text = stringResource(id = CoreR.string.data_collection_consent)) },
        text = {
            DialogConsentContent(
                onAgreeClick = onAgreeClick,
                onDisagreeClick = onDisagreeClick
            )
        },
        onDismissRequest = {},
        confirmButton = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Composable
private fun DialogConsentContent(
    modifier: Modifier = Modifier,
    onAgreeClick: () -> Unit = {},
    onDisagreeClick: () -> Unit = {}
) {
    val spaceLarge = MaterialTheme.spacing.large

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.height(400.dp)
    ) {
        Text(
            text = stringResource(id = CoreR.string.data_collection_consent_description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        )
        Spacer(modifier = Modifier.height(spaceLarge))
        ConsentButtons(
            onAgreeClick = onAgreeClick,
            onDisagreeClick = onDisagreeClick
        )
    }
}

@Composable
private fun ConsentButtons(
    modifier: Modifier = Modifier,
    onAgreeClick: () -> Unit = {},
    onDisagreeClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ConsentButton(
            text = stringResource(id = CoreR.string.data_collection_consent_agree),
            onClick = onAgreeClick,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = 8.dp,
                bottomEnd = 8.dp
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        ConsentButton(
            text = stringResource(id = CoreR.string.data_collection_consent_disagree),
            onClick = onDisagreeClick,
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        )
    }
}

@Composable
private fun ConsentButton(
    text: String,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit
) {
    val buttonColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    Surface(
        onClick = onClick,
        color = buttonColor,
        shape = shape,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
private fun AnalyticsCollectionConsentContentPreview() {
    NewQuizTheme {
        Surface {
            DataCollectionConsentDialog()
        }
    }
}
