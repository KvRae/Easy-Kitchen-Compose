package com.kvrae.easykitchen.presentation.miscellaneous.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .height(100.dp)
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(navItems.size) { index ->
            val isSelected = navItems[index].name == navItem

            val iconColor = animateColorAsState(
                targetValue = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "Icon Color Animation"
            )

            val iconScale = animateFloatAsState(
                targetValue = if (isSelected) 1.15f else 1.0f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "Icon Scale Animation"
            )

            val backgroundColor = animateColorAsState(
                targetValue = if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                else
                    Color.Transparent,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "Background Color Animation"
            )

            val labelAlpha = animateFloatAsState(
                targetValue = if (isSelected) 1.0f else 0.7f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "Label Alpha Animation"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(backgroundColor.value)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .animateContentSize(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
            ) {
                IconButton(
                    onClick = { onNavItemSelect(navItems[index].name) },
                    modifier = Modifier.size(44.dp),
                    interactionSource = remember { MutableInteractionSource() },
                    content = {
                        Icon(
                            modifier = Modifier
                                .size(28.dp)
                                .scale(iconScale.value),
                            painter = painterResource(getIcon(navItems[index], navItem)),
                            contentDescription = navItems[index].name,
                            tint = iconColor.value
                        )
                    }
                )

                // Label below icon
                if (isSelected) {
                    Text(
                        text = navItems[index].name,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        color = iconColor.value.copy(alpha = labelAlpha.value),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
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

