package com.kvrae.easykitchen.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.RestaurantMenu
import androidx.compose.material.icons.rounded.SignalWifiConnectedNoInternet4
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kvrae.easykitchen.presentation.miscellaneous.screens.NoDataScreen
import com.kvrae.easykitchen.utils.rememberNetworkConnectivity
import com.kvrae.easykitchen.utils.stripMarkdown
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuickTipItem(emoji: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.width(32.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<ChatViewModel>()
    val chatState by viewModel.chatState.collectAsState()
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val isNetworkOn = rememberNetworkConnectivity()

    if (!isNetworkOn) {
        NoDataScreen(
            message = "No internet connection",
            subtitle = "Connect to the internet to chat with your AI Chef",
            icon = Icons.Rounded.SignalWifiConnectedNoInternet4
        )
        return
    }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        // Chat Messages
        if (viewModel.chatMessages.isEmpty() && chatState !is ChatState.Loading) {
            // Empty State
            EmptyChatPlaceHolder()
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.chatMessages.size) { index ->
                    MessageItem(message = viewModel.chatMessages[index])
                }

                if (chatState is ChatState.Loading) {
                    item {
                        ChefTypingIndicator()
                    }
                }
            }
        }

        // Input Area
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        viewModel.userMessage = it.text
                    },
                    placeholder = {
                        Text(
                            text = "Message your Chef...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(28.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                            alpha = 0.15f
                        ),
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                            alpha = 0.1f
                        ),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 4,
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                val isNotEmpty = textFieldValue.text.isNotBlank()

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (isNotEmpty) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        .clickable(enabled = isNotEmpty) {
                            viewModel.sendMessage()
                            textFieldValue = TextFieldValue("")
                            focusManager.clearFocus()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Send",
                        tint = if (isNotEmpty) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    LaunchedEffect(viewModel.chatMessages.size) {
        if (viewModel.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(viewModel.chatMessages.size - 1)
        }
    }
}

@Composable
private fun ColumnScope.EmptyChatPlaceHolder() {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        // Placeholder Icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.RestaurantMenu,
                contentDescription = "Chef",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Empty State Title
        Text(
            text = "Start Cooking Adventure",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Empty State Description
        Text(
            text = "Ask your AI Chef Assistant anything about recipes, cooking tips, or meal ideas!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Tips
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickTipItem("🍳", "Ask about recipe ideas")
            QuickTipItem("⏱️", "Get cooking time estimates")
            QuickTipItem("🥘", "Learn cooking techniques")
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val isUser = message.role == Role.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {

        if (!isUser) {
            ChefAvatar()
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isUser) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                )
                .padding(12.dp)
        ) {
            Text(
                text = if (isUser) message.content else message.content.stripMarkdown(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun ChefAvatar() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.RestaurantMenu,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ChefTypingIndicator() {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChefAvatar()
        Spacer(modifier = Modifier.width(8.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Text(
            text = " Chef is thinking...",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
