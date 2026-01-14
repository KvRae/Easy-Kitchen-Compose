package com.kvrae.easykitchen.presentation.meals

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.data.remote.dto.asMealDetail
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

    var isRefreshing = mealState is MealState.Loading
    val refreshingState = rememberPullToRefreshState()



    PullToRefreshBox(
        modifier = Modifier.statusBarsPadding(),
        isRefreshing =  isRefreshing,
        state = refreshingState,
        onRefresh = {
            mealsViewModel.fetchMeals()
        }
    ) {
        when (mealState) {
            is MealState.Loading -> MealsImageCoveredListLoad(modifier)
            is MealState.Error -> NoDataScreen(message = (mealState as MealState.Error).message)
            is MealState.Success -> MealScreenContent(
                modifier = modifier,
                mealList = (mealState as MealState.Success).data,
                onMealClick = { mealId ->
                    navController.navigate("${MEAL_DETAILS_SCREEN_ROUTE}/$mealId")
                },
                onFavoriteClick = { _ ->
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
    onFavoriteClick: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
    ) {
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

