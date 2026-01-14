package com.kvrae.easykitchen.presentation.miscellaneous.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kvrae.easykitchen.R
import com.valentinilk.shimmer.shimmer

@Composable
fun GlowButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
    enabled: Boolean = true,
    text: String = stringResource(R.string.empty_string),
) {
    Button(
        modifier = Modifier
            .shimmer()
            .fillMaxSize(),
        onClick = {onClick},
        content = {content},
    )
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.background,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun FormButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    text: String = stringResource(R.string.empty_string),
) {
    Button(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
        ,
        onClick = onClick,
        enabled = enabled,
    ){
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.Bold,
            )
    }

}

@Composable
fun TextFormButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    text: String = stringResource(R.string.empty_string),
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun TextBoxForm(
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit = {},
    enabled: Boolean = true,
    text: String = stringResource(R.string.empty_string),
    ) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Start
    ) {
        Checkbox(
            checked = enabled,
            onCheckedChange = onClick,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}