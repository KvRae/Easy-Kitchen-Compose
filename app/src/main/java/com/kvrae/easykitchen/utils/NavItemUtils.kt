package com.kvrae.easykitchen.utils

import com.kvrae.easykitchen.R

data class NavItem(
    val name: String,
    val iconFilled: Int,
    val iconOutline: Int,
    val title: String = name,
    val description: String = ""
)

val navItems = listOf(
    NavItem(
        MAIN_HOME_ROUTE,
        R.drawable.restaurant_filled,
        R.drawable.restaurant_outline,
        "Hello there!",
        "Welcome to EasyKitchen"
    ),
    NavItem(
        MAIN_MEALS_ROUTE,
        R.drawable.book_filled,
        R.drawable.book_outlined,
        "Meals",
        "Discover new recipes!"
    ),
    NavItem(
        MAIN_COMPOSE_ROUTE,
        R.drawable.bowl_filled,
        R.drawable.bowl_outline,
        "Pantry Match",
        "Pick ingredients and see what you can cook"
    ),
    NavItem(
        MAIN_CHAT_ROUTE,
        R.drawable.chef_hat_filled,
        R.drawable.chef_hat_outlined,
        "AI Chef Assistant",
        "Ask me anything about cooking!"
    )
)

fun getNavItemByName(name: String): NavItem? {
    return navItems.find {
        it.name == name
    }
}