package com.kvrae.easykitchen.presentation.meals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.data.remote.dto.asMealDetail
import com.kvrae.easykitchen.domain.model.FilterOptions
import com.kvrae.easykitchen.domain.model.MealFilter
import com.kvrae.easykitchen.presentation.meals.components.FilterBottomSheet
import com.kvrae.easykitchen.presentation.meals.components.MealSearchBar
import com.kvrae.easykitchen.presentation.miscellaneous.components.MealImageCoveredCard
import com.kvrae.easykitchen.presentation.miscellaneous.screens.MealsImageCoveredListLoad
import com.kvrae.easykitchen.presentation.miscellaneous.screens.NoDataScreen
import com.kvrae.easykitchen.utils.MEAL_DETAILS_SCREEN_ROUTE
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val mealsViewModel = koinViewModel<MealsViewModel>()
    val mealState by mealsViewModel.mealState.collectAsState()
    val currentFilter by mealsViewModel.currentFilter.collectAsState()
    val filterOptions by mealsViewModel.filterOptions.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshingState = rememberPullToRefreshState()
    var showFilterSheet by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MealSearchBar(
            searchQuery = currentFilter.searchQuery,
            onSearchQueryChange = { query ->
                mealsViewModel.updateSearchQuery(query)
            },
            onFilterClick = { showFilterSheet = true },
            hasActiveFilters = mealsViewModel.hasActiveFilters()
        )
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = refreshingState,
        onRefresh = {
            mealsViewModel.fetchMeals()
        }
    ) {
        when (mealState) {
            is MealState.Loading -> MealsImageCoveredListLoad()
            is MealState.Error -> NoDataScreen(message = (mealState as MealState.Error).message)
            is MealState.Success -> MealScreenContent(
                mealList = (mealState as MealState.Success).data,
                onMealClick = { mealId ->
                    navController.navigate("${MEAL_DETAILS_SCREEN_ROUTE}/$mealId")
                },
                onFavoriteClick = { _ ->
                },
                mealsViewModel = mealsViewModel,
                currentFilter = currentFilter,
                filterOptions = filterOptions,
            )
        }
        }
        if (showFilterSheet) {
            FilterBottomSheet(
                currentFilter = currentFilter,
                availableCategories = filterOptions.availableCategories,
                availableAreas = filterOptions.availableAreas,
                onDismiss = { showFilterSheet = false },
                onApplyFilter = { filter ->
                    mealsViewModel.applyFilter(filter)
                }
            )
        }
    }
}

@Composable
fun MealScreenContent(
    modifier: Modifier = Modifier,
    mealList: List<MealResponse>,
    onMealClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit = {},
    mealsViewModel: MealsViewModel = koinViewModel(),
    currentFilter: MealFilter = mealsViewModel.currentFilter.collectAsState().value,
    filterOptions: FilterOptions = mealsViewModel.filterOptions.collectAsState().value,
) {

    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        // Search Bar with Filter Button
        items(
            count = mealList.size,
            key = { index -> mealList[index].idResponse ?: index },
        ) { index ->
            MealImageCoveredCard(
                meal = mealList[index].asMealDetail(),
                onMealClick = {
                    onMealClick(mealList[index].asMealDetail().id ?: "")
                },
                onFavoriteClick = {
                    onFavoriteClick(mealList[index].idResponse ?: "")
                }
            )
        }
        }
}

