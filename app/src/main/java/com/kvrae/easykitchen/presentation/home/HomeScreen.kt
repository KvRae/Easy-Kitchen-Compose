package com.kvrae.easykitchen.presentation.home


import SearchBarField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.data.remote.dto.CategoryResponse
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.data.remote.dto.asDto
import com.kvrae.easykitchen.presentation.miscellaneous.components.CategoryCard
import com.kvrae.easykitchen.presentation.miscellaneous.components.HorizontalList
import com.kvrae.easykitchen.presentation.miscellaneous.components.MealByAreaAnsCategoryCard
import com.kvrae.easykitchen.presentation.miscellaneous.components.MealCard
import com.kvrae.easykitchen.presentation.miscellaneous.screens.LottieAnimation
import com.kvrae.easykitchen.presentation.miscellaneous.screens.NoDataScreen
import com.kvrae.easykitchen.utils.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,

) {
    val viewModel = koinViewModel<HomeViewModel>()
    val homeState by viewModel.homeState.collectAsState()
    val isRefreshing by remember {
        mutableStateOf(false)
    }
    val refreshingState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = Modifier.statusBarsPadding(),
        isRefreshing = isRefreshing,
        state = refreshingState,
        onRefresh = {
            viewModel.getData(forceRefresh = true)
        }
    ) {
        when(homeState){
            is HomeState.Loading -> LottieAnimation(
                rawRes = R.raw.food_loading,
            )
            is HomeState.Success -> HomeScreenContent(
                modifier = modifier,
                navController = navController,
                meals = (homeState as HomeState.Success).meals,
                categories = (homeState as HomeState.Success).categories,
                viewModel = viewModel
            )
            is HomeState.Error -> NoDataScreen(
                message = (homeState as HomeState.Error).message,
                icon = Icons.Rounded.SearchOff,
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    categories : List<CategoryResponse>,
    meals: List<MealResponse>,
    viewModel: HomeViewModel
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        SearchBarField(
            modifier = Modifier,
            navController = navController,
            placeholder = stringResource(id = R.string.search_meal),
        )

        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(categories) { category ->
                CategoryCard(category = category.asDto())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val (timeTitle, timeMeals) = remember {
            viewModel.getMealsByTimeOfDay(meals)
        }
        HorizontalList(
            title = timeTitle,
            content = {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(timeMeals) { meal ->
                        MealCard(
                            meal = meal.asDto(),
                            onMealClick = {
                                navController.navigate("${Screen.MealDetailsScreen.route}/$it")
                            }
                        )
                    }
                }
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        val (locationTitle, locationMeals) = viewModel.getLocationSection(meals)
        HorizontalList(
            title = locationTitle,
            content = {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(locationMeals) { meal ->
                        MealByAreaAnsCategoryCard(
                            meal = meal.asDto(),
                            onMealClick = {
                                navController.navigate("${Screen.MealDetailsScreen.route}/$it")
                            }
                        )
                    }
                }
            },
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
