package com.kvrae.easykitchen.presentation.miscellaneous.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kvrae.easykitchen.utils.MAIN_CHAT_ROUTE
import com.kvrae.easykitchen.utils.MAIN_COMPOSE_ROUTE
import com.kvrae.easykitchen.utils.MAIN_HOME_ROUTE
import com.kvrae.easykitchen.utils.MAIN_MEALS_ROUTE
import com.kvrae.easykitchen.utils.NavItem

@Composable
fun BottomNavBar(
    navItems: List<NavItem>,
    navItem: String,
    onNavItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),

        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(navItems.size) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { onNavItemSelect(navItems[index].name) },
                    content = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(getIcon(navItems[index], navItem)),
                            contentDescription = navItems[index].name,
                            tint = if (navItems[index].name == navItem)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = navItems[index].name,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (navItems[index].name == navItem)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }


        }
    }

}

fun getIcon(
    navItem: NavItem,
    selectedNavItem: String
): Int {
    return when (navItem.name) {
        MAIN_HOME_ROUTE -> if (navItem.name == selectedNavItem) {
            navItem.iconFilled
        } else {
            navItem.iconOutline
        }
        MAIN_MEALS_ROUTE -> if (navItem.name == selectedNavItem) {
            navItem.iconFilled
        } else {
            navItem.iconOutline
        }
        MAIN_COMPOSE_ROUTE -> if (navItem.name == selectedNavItem) {
            navItem.iconFilled
        } else {
            navItem.iconOutline
        }
        MAIN_CHAT_ROUTE -> if (navItem.name == selectedNavItem) {
            navItem.iconFilled
        } else {
            navItem.iconOutline
        }
        else -> navItem.iconOutline
    }
}

