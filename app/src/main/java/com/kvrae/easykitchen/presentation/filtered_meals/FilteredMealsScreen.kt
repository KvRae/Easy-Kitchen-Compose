package com.kvrae.easykitchen.presentation.filtered_meals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.data.remote.dto.asMealDetail
import com.kvrae.easykitchen.presentation.miscellaneous.components.MealImageCoveredCard
import com.kvrae.easykitchen.presentation.miscellaneous.screens.CircularLoadingScreen
import com.kvrae.easykitchen.presentation.miscellaneous.screens.NoDataScreen
import com.kvrae.easykitchen.utils.MEAL_DETAILS_SCREEN_ROUTE
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilteredMealsScreen(
    navController: NavController,
    selectedIngredientNames: List<String>,
    categoryName: String? = null,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<FilteredMealsViewModel>()
    val filteredMealsState by viewModel.filteredMealsState.collectAsState()
    val exactMatches by viewModel.exactMatches.collectAsState()
    val partialMatches by viewModel.partialMatches.collectAsState()

    // Trigger filtering when screen loads
    LaunchedEffect(selectedIngredientNames, categoryName) {
        when {
            !categoryName.isNullOrBlank() -> viewModel.filterMealsByCategory(categoryName)
            selectedIngredientNames.isNotEmpty() -> viewModel.filterMealsByIngredients(
                selectedIngredientNames
            )

            else -> viewModel.filterMealsByCategory("")
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = categoryName?.let { "Meals in $it" } ?: "Recipes Found",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        when (filteredMealsState) {
            is FilteredMealsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularLoadingScreen()
                }
            }

            is FilteredMealsState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    NoDataScreen(
                        message = (filteredMealsState as FilteredMealsState.Error).message
                    )
                }
            }

            is FilteredMealsState.Success -> {
                val primaryTitle =
                    if (!categoryName.isNullOrBlank()) "Meals in $categoryName" else "Perfect Matches"
                val primarySubtitle =
                    if (!categoryName.isNullOrBlank()) "Fresh picks from $categoryName" else "Recipes with all your ingredients"
                FilteredMealsContent(
                    modifier = Modifier.padding(paddingValues),
                    exactMatches = exactMatches,
                    partialMatches = partialMatches,
                    onMealClick = { mealId ->
                        navController.navigate("$MEAL_DETAILS_SCREEN_ROUTE/$mealId")
                    },
                    primaryTitle = primaryTitle,
                    primarySubtitle = primarySubtitle
                )
            }
        }
    }
}

@Composable
fun FilteredMealsContent(
    exactMatches: List<MealResponse>,
    partialMatches: List<MealResponse>,
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    primaryTitle: String = "Perfect Matches",
    primarySubtitle: String = "Recipes with all your ingredients"
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Exact Matches Section
        if (exactMatches.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = primaryTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$primarySubtitle (${exactMatches.size})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(
                count = exactMatches.size,
                key = { index -> "exact_${exactMatches[index].idResponse ?: index}" }
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    MealImageCoveredCard(
                        meal = exactMatches[index].asMealDetail(),
                        onMealClick = {
                            onMealClick(exactMatches[index].idResponse ?: "")
                        },
                        onFavoriteClick = {}
                    )
                }
            }
        }

        // Partial Matches Section
        if (partialMatches.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Other Suggestions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Recipes with some of your ingredients (${partialMatches.size})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(
                count = partialMatches.size,
                key = { index -> "partial_${partialMatches[index].idResponse ?: index}" }
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    MealImageCoveredCard(
                        meal = partialMatches[index].asMealDetail(),
                        onMealClick = {
                            onMealClick(partialMatches[index].idResponse ?: "")
                        },
                        onFavoriteClick = {}
                    )
                }
            }
        }
    }
}
