package com.kvrae.easykitchen.presentation.home


import SearchBarField
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.data.remote.dto.CategoryResponse
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.data.remote.dto.asDto
import com.kvrae.easykitchen.presentation.miscellaneous.components.CategoryCard
import com.kvrae.easykitchen.presentation.miscellaneous.components.MealByAreaAnsCategoryCard
import com.kvrae.easykitchen.presentation.miscellaneous.components.MealCard
import com.kvrae.easykitchen.presentation.miscellaneous.screens.CircularLoadingScreen
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
            is HomeState.Loading -> CircularLoadingScreen()
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
        // Search Bar Section
        SearchBarField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            navController = navController,
            placeholder = stringResource(id = R.string.search_meal),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Featured Meal Section (Hero)
        val featuredMeal = viewModel.getFeaturedMeal(meals)
        if (featuredMeal != null) {
            SectionHeader(
                title = "Continue where you left off",
                icon = ImageVector.vectorResource(id = R.drawable.backup_restore)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MealCard(
                meal = featuredMeal.asDto(),
                onMealClick = { mealId ->
                    viewModel.onMealViewed(mealId)
                    navController.navigate("${Screen.MealDetailsScreen.route}/$mealId")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Categories Section with Header
        SectionHeader(
            title = "Categories",
            icon = Icons.Rounded.Restaurant
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category.asDto(),
                    onCategoryClick = { name ->
                        val encoded = Uri.encode(name)
                        navController.navigate("${Screen.FilteredMealsScreen.route}/category/$encoded")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Time-Based Meals Section with Header
        val (timeTitle, timeMeals) = remember {
            viewModel.getMealsByTimeOfDay(meals)
        }

        SectionHeader(
            title = timeTitle,
            icon = Icons.Rounded.Schedule
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(timeMeals) { meal ->
                MealCard(
                    meal = meal.asDto(),
                    onMealClick = { mealId ->
                        viewModel.onMealViewed(mealId)
                        navController.navigate("${Screen.MealDetailsScreen.route}/$mealId")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Location-Based Meals Section with Header
        val (locationTitle, locationMeals) = viewModel.getLocationSection(meals)

        SectionHeader(
            title = locationTitle,
            icon = Icons.Rounded.Restaurant
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(locationMeals) { meal ->
                MealByAreaAnsCategoryCard(
                    meal = meal.asDto(),
                    onMealClick = { mealId ->
                        viewModel.onMealViewed(mealId)
                        navController.navigate("${Screen.MealDetailsScreen.route}/$mealId")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
