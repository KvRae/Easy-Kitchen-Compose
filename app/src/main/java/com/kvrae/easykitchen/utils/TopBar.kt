package com.kvrae.easykitchen.utils

import com.kvrae.easykitchen.R

fun getTopBarTitle(
    navItem: NavItem,
    selectedNavItem: String
): String {
    return when (navItem.name) {
        MAIN_HOME_ROUTE -> "Home"
        MAIN_MEALS_ROUTE -> "Meals"
        MAIN_COMPOSE_ROUTE -> "Compose"
        MAIN_CHAT_ROUTE -> "Chat"
        else -> "EasyKitchen"
    }
}

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
        MAIN_MEALS_ROUTE -> R.drawable.round_bookmark
        MAIN_COMPOSE_ROUTE -> R.drawable.round_shopping_bag
        else -> R.drawable.outline_event_list
    }
}