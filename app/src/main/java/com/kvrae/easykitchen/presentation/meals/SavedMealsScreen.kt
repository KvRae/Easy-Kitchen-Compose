package com.kvrae.easykitchen.presentation.meals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.data.local.entity.SavedMeal
import com.kvrae.easykitchen.data.remote.dto.MealDetail
import com.kvrae.easykitchen.presentation.miscellaneous.components.CustomAlertDialogWithContent
import com.kvrae.easykitchen.presentation.miscellaneous.components.MealImageCoveredCard
import com.kvrae.easykitchen.presentation.miscellaneous.screens.NoDataScreen
import com.kvrae.easykitchen.utils.MEAL_DETAILS_SCREEN_ROUTE
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMealsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<SavedMealsViewModel>()
    val savedMeals by viewModel.savedMeals.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                title = {
                    Column {
                        Text(
                            text = "Saved meals",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Meals you marked as favorites",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier.background(MaterialTheme.colorScheme.surface)
    ) { padding ->
        if (savedMeals.isEmpty()) {
            NoDataScreen(
                modifier = Modifier.padding(padding),
                message = "No saved meals yet",
                subtitle = "Tap the heart on any meal to add it here"
            )
        } else {
            SavedMealsList(
                meals = savedMeals,
                padding = padding,
                onMealClick = { id ->
                    navController.navigate("${MEAL_DETAILS_SCREEN_ROUTE}/$id")
                }
            )
        }
    }
}

@Composable
private fun SavedMealsList(
    meals: List<SavedMeal>,
    padding: PaddingValues,
    onMealClick: (String) -> Unit
) {
    val mealsViewModel = koinViewModel<MealsViewModel>()
    val savedMealsViewModel = koinViewModel<SavedMealsViewModel>()

    // Confirmation dialog state
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val mealToRemove = remember { mutableStateOf<SavedMeal?>(null) }

    // Confirmation Dialog
    if (showConfirmationDialog.value && mealToRemove.value != null) {
        CustomAlertDialogWithContent(
            title = "Remove from Favorites",
            content = {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Are you sure you want to remove \"${mealToRemove.value?.name}\" from your favorites?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            confirmText = "Remove",
            dismissText = "Cancel",
            onDismiss = {
                showConfirmationDialog.value = false
                mealToRemove.value = null
            },
            onConfirm = {
                mealToRemove.value?.let { meal ->
                    // Remove from favorites using SavedMealsViewModel
                    savedMealsViewModel.remove(meal.id ?: "")
                    // Also toggle in MealsViewModel for consistency
                    val mealResponse = mealsViewModel.findMealById(meal.id)
                    mealResponse?.let { mealsViewModel.toggleFavorite(it) }
                }
                showConfirmationDialog.value = false
                mealToRemove.value = null
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = padding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(meals, key = { it.id ?: it.idTable }) { meal ->
            MealImageCoveredCard(
                meal = meal.asMealDetail(),
                onMealClick = { onMealClick(meal.id ?: "") },
                isFavorite = true,
                onFavoriteClick = {
                    // Show confirmation dialog before removing
                    mealToRemove.value = meal
                    showConfirmationDialog.value = true
                }
            )
        }
    }
}

private fun SavedMeal.asMealDetail(): MealDetail = MealDetail(
    id = id,
    name = name,
    category = category,
    area = area,
    instructions = instructions,
    image = image,
    youtube = youtube,
    ingredients = ingredients,
    measures = measures
)
