package com.infinitepower.newquiz.compose.ui.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.infinitepower.newquiz.compose.ui.login.LoginButtonData

@Composable
internal fun LoginButtons(
    modifier: Modifier = Modifier,
    buttons: List<LoginButtonData>,
    onClick: (item: LoginButtonData) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = buttons,
            key = { it.key }
        ) { item ->
            LoginButtonComponent(
                modifier = Modifier.fillParentMaxWidth(.7f),
                data = item,
                onClick = { onClick(item) }
            )
        }
    }
}

@Composable
private fun LoginButtonComponent(
    modifier: Modifier = Modifier,
    data: LoginButtonData,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = rememberAsyncImagePainter(model = data.icon),
            contentDescription = data.name,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = data.name)
    }
}