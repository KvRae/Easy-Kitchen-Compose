package com.kvrae.easykitchen.presentation.miscellaneous.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kvrae.easykitchen.utils.MAIN_COMPOSE_ROUTE
import com.kvrae.easykitchen.utils.getTapBarIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    name: String = "EasyKitchen",
    title: String? = null,
    description: String? = null,
    onActionClick: () -> Unit,
    actionIcon: Int = getTapBarIcon(name),
    ingredientsSize : Int = 0
) {
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
                if (title != null)
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                if (description != null)
                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
            }
        },
        actions = {
            if (title != MAIN_COMPOSE_ROUTE) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        painter =
                            painterResource(id = actionIcon),
                        contentDescription = "actionIcon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                BadgedBox(
                    modifier = Modifier.padding(end = 16.dp),
                    badge = {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .size(16.dp)


                    ) {

                    }
                }) {
                    Icon(
                        painter = painterResource(id = actionIcon),
                        contentDescription = "actionIcon"
                    )
                }
            }


        },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    )
}



