package com.kvrae.easykitchen.domain.model

data class MealFilter(
    val searchQuery: String = "",
    val categories: Set<String> = emptySet(),
    val areas: Set<String> = emptySet(),
    val sortBy: SortOption = SortOption.NAME_ASC
)

enum class SortOption {
    NAME_ASC,
    NAME_DESC,
    CATEGORY,
    AREA
}

data class FilterOptions(
    val availableCategories: List<String> = emptyList(),
    val availableAreas: List<String> = emptyList()
)

