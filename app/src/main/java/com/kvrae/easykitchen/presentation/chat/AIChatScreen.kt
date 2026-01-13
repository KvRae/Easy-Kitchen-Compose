package com.kvrae.easykitchen.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.kvrae.easykitchen.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<ChatViewModel>()
    val chatState = viewModel.chatState.collectAsState()
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.Start)
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState
        ) {
           items(viewModel.chatMessages.size) { index ->
               val message = viewModel.chatMessages[index]
               MessageItem(message = message)

           }
            item {
                if (chatState.value is ChatState.Loading)
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .size(24.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
            val fieldFocus = LocalFocusManager.current

            OutlinedTextField(
                value = textFieldValue,
                placeholder = { Text(text = stringResource(id = R.string.message)) },
                colors = TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                ),
                trailingIcon ={
                    IconButton(onClick = {
                        viewModel.sendMessage()
                        textFieldValue = TextFieldValue("")
                        fieldFocus.clearFocus()
                    }) {
                        Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Send")
                    }
                },
                onValueChange = { textFieldValue = it; viewModel.userMessage = it.text },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp)
            )
        }
    }
    LaunchedEffect(viewModel.chatMessages.size) {
            if (viewModel.chatMessages.isNotEmpty())
                listState.animateScrollToItem(viewModel.chatMessages.size - 1)
    }
}

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    message: Message
) {
    val messageDirection = if (message.role == Role.USER)
        Arrangement.End
     else
        Arrangement.Start

    val messageColor = if (message.role == Role.USER)
        MaterialTheme.colorScheme.tertiaryContainer
     else
        MaterialTheme.colorScheme.tertiary

    val messageTextColor = if (message.role == Role.USER)
        MaterialTheme.colorScheme.inverseSurface
     else
        MaterialTheme.colorScheme.background

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = messageDirection,
        verticalAlignment = Alignment.CenterVertically
    ){
       Box(
           modifier = Modifier
               .clip(RoundedCornerShape(16.dp))
               .background(messageColor)
               .padding(8.dp),
           content = {
               Text(
                   text = message.content,
                   style = MaterialTheme.typography.bodyMedium,
                   color = messageTextColor
               )
           }
       )

    }


}