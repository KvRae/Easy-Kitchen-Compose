package com.kvrae.easykitchen.utils

import com.kvrae.easykitchen.R

data class NavItem(
    val name: String,
    val iconFilled: Int,
    val iconOutline: Int
)

val navItems = listOf(
    NavItem( MAIN_HOME_ROUTE, R.drawable.baseline_home_filled,R.drawable.rounded_home_outlined),
    NavItem(MAIN_MEALS_ROUTE, R.drawable.round_fastfood__filled, R.drawable.outline_fastfood_round),
    NavItem(MAIN_COMPOSE_ROUTE, R.drawable.rice_bowl_filled, R.drawable.outline_rice_bowl),
    NavItem(MAIN_CHAT_ROUTE, R.drawable.baseline_chat_bubble_24, R.drawable.round_chat_bubble_outline_24)
)