package com.kvrae.easykitchen.presentation.ingrendient

import SearchField
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.data.remote.dto.IngredientResponse
import com.kvrae.easykitchen.data.remote.dto.asDto
import com.kvrae.easykitchen.presentation.miscellaneous.components.IngredientCard
import com.kvrae.easykitchen.presentation.miscellaneous.screens.CircularLoadingScreen
import com.kvrae.easykitchen.presentation.miscellaneous.screens.NoDataScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun IngredientsScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    val viewModel = koinViewModel<IngredientViewModel>()
    val filteredIngredientState by viewModel.filteredIngredientsState.collectAsState()
    val ingredientsState by viewModel.ingredientsState.collectAsState()
    val basketCount by viewModel.basketCount.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberPullToRefreshState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top,
        ) {
            SearchField(
                query = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onClearClick = { viewModel.clearSearch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 8.dp, end = 8.dp)
            )
            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                state = swipeRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.getIngredients()
                    isRefreshing = false
                }
            ) {

                when (ingredientsState) {
                    is IngredientState.Loading -> CircularLoadingScreen()
                    is IngredientState.Success -> {
                        val filteredIngredients =
                            (filteredIngredientState as IngredientState.Success).ingredients
                        if (filteredIngredients.isEmpty() && searchQuery.isNotEmpty()) {
                            NoDataScreen(message = "No ingredients found matching \"$searchQuery\"")
                        } else {
                            IngredientScreenContent(
                                modifier = modifier,
                                viewModel = viewModel,
                                ingredients = filteredIngredients,
                                searchQuery = searchQuery
                            )
                        }
                    }

                    is IngredientState.Error -> NoDataScreen(
                        message = (filteredIngredientState as IngredientState.Error).message
                    )
                }
            }
        }

        // Floating Action Button for finding meals
        AnimatedVisibility(
            visible = basketCount >= 2,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    val selectedIngredients = viewModel.getSelectedIngredientNames()
                    navController?.navigate(
                        "filtered_meals/${selectedIngredients.joinToString(",")}"
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Find Recipes",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Find Recipes ($basketCount)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}



@Composable
fun IngredientScreenContent(
    viewModel: IngredientViewModel,
    ingredients: List<IngredientResponse>,
    modifier: Modifier = Modifier,
    searchQuery: String = ""
) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize(),
            columns = StaggeredGridCells.Adaptive(150.dp)
        ) {
            items(
                count = ingredients.size,
                key = { index -> ingredients[index].idResponse ?: index }
            ) { index ->
                IngredientCard(
                    ingredient = ingredients[index].asDto(),
                    onIngredientClick = {
                        viewModel.updateIngredientInBasket(
                            ingredient = ingredients[index]
                        )
                    },
                    isChecked = viewModel.isIngredientInBasket(
                        ingredient = ingredients[index]
                    )
                )
            }
        }
}

@Preview
@Composable
private fun IngredientScreenPreview() {
    IngredientsScreen()
}
