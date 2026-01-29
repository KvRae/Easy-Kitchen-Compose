package com.kvrae.easykitchen.presentation.miscellaneous.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kvrae.easykitchen.utils.MAIN_CHAT_ROUTE
import com.kvrae.easykitchen.utils.MAIN_COMPOSE_ROUTE
import com.kvrae.easykitchen.utils.MAIN_HOME_ROUTE
import com.kvrae.easykitchen.utils.MAIN_MEALS_ROUTE
import com.kvrae.easykitchen.utils.getTapBarIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    name: String = "EasyKitchen",
    title: String? = null,
    description: String? = null,
    onActionClick: () -> Unit,
    onResetChat: (() -> Unit)? = null,
    actionIcon: Int = getTapBarIcon(name),
    ingredientsSize: Int = 0,
    username: String = "",
    isLimitReached: Boolean = false
) {
    val showResetDialog = remember { mutableStateOf(false) }

    // Reset Confirmation Dialog
    if (showResetDialog.value) {
        AlertDialog(
            onDismissRequest = { showResetDialog.value = false },
            title = {
                Text(
                    text = "Clear Chat",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to clear all chat messages? This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog.value = false }) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onResetChat?.invoke()
                    showResetDialog.value = false
                }) {
                    Text(text = "Clear All")
                }
            }
        )
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (title != null) {
                    // Show "Welcome, [username]" for Home screen if username is available
                    if (name == MAIN_HOME_ROUTE && username.isNotEmpty()) {
                        Text(
                            text = "Welcome, $username",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                if (description != null)
                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
            }
        },
        actions = {
            if (name == MAIN_CHAT_ROUTE) {
                // Chat screen - show reset button only if limit not reached
                if (!isLimitReached) {
                    IconButton(onClick = { showResetDialog.value = true }) {
                        Icon(
                            painter = painterResource(id = actionIcon),
                            contentDescription = "Reset Chat",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            } else if (name == MAIN_COMPOSE_ROUTE) {
                // Ingredients screen - show basket with badge
                IconButton(onClick = onActionClick) {
                    BadgedBox(
                        badge = {
                            if (ingredientsSize > 0) {
                                androidx.compose.material3.Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(
                                        text = ingredientsSize.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = actionIcon),
                            contentDescription = "Basket with $ingredientsSize ingredients",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            } else {
                IconButton(onClick = onActionClick) {
                    if (name == MAIN_MEALS_ROUTE) {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = "Favorites",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = actionIcon),
                            contentDescription = "actionIcon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

        },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    )
}
