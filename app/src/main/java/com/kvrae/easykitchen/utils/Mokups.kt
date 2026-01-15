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
        R.drawable.baseline_home_filled,
        R.drawable.rounded_home_outlined,
        "Hello there!",
        "Welcome to EasyKitchen"
    ),
    NavItem(
        MAIN_MEALS_ROUTE,
        R.drawable.round_fastfood__filled,
        R.drawable.outline_fastfood_round,
        "Meals",
        "Discover new recipes!"
    ),
    NavItem(
        MAIN_COMPOSE_ROUTE,
        R.drawable.rice_bowl_filled,
        R.drawable.outline_rice_bowl,
        "Meal Planner",
        "Pick ingredients and look for recipes!"
    ),
    NavItem(
        MAIN_CHAT_ROUTE,
        R.drawable.baseline_chat_bubble_24,
        R.drawable.round_chat_bubble_outline_24,
        "AI Chef Assistant",
        "Ask me anything about cooking!"
    )
)

fun getNavItemByName(name: String): NavItem? {
    return navItems.find {
        it.name == name
    }
}