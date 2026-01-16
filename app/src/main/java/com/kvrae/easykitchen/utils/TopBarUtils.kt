package com.kvrae.easykitchen.utils

import com.kvrae.easykitchen.R


fun getTopBarClickAction(
    navItem: NavItem,
    selectedNavItem: String
) {
    return when (navItem.name) {
        MAIN_HOME_ROUTE -> { }
        MAIN_MEALS_ROUTE -> { }
        MAIN_COMPOSE_ROUTE ->{ }
        MAIN_CHAT_ROUTE -> { }
        else -> { }
    }
}

fun getTapBarIcon(
    navItem: String,
): Int {
    return when (navItem) {
        MAIN_MEALS_ROUTE -> R.drawable.ic_favorite_meals
        MAIN_COMPOSE_ROUTE -> R.drawable.rice_bowl_filled
        MAIN_CHAT_ROUTE -> R.drawable.outline_refresh_24
        else -> R.drawable.rounded_side_navigation_24
    }
}