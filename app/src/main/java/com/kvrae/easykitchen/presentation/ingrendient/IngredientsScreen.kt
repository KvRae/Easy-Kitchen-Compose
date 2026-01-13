package com.kvrae.easykitchen.presentation.ingrendient

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kvrae.easykitchen.data.remote.dto.asDto
import com.kvrae.easykitchen.presentation.miscellaneous.components.IngredientCard
import com.kvrae.easykitchen.presentation.miscellaneous.screens.CircularLoadingScreen
import com.kvrae.easykitchen.presentation.miscellaneous.screens.NoDataScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun IngredientsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,

) {
    val viewModel = koinViewModel<IngredientViewModel>()
    val ingredientState by viewModel.ingredientsState.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        state =  swipeRefreshState,
        onRefresh = {
            isRefreshing = true
            viewModel.getIngredients()
            isRefreshing = false
        }
    ) {
        when (ingredientState) {
            is IngredientState.Loading -> CircularLoadingScreen()
            is IngredientState.Success -> IngredientScreenContent(
                modifier = modifier,
                viewModel = viewModel
            )
            is IngredientState.Error -> NoDataScreen(
                message = (ingredientState as IngredientState.Error).message
            )
        }
    }


}

@Composable
fun IngredientScreenContent(
    viewModel: IngredientViewModel,
    modifier: Modifier = Modifier
) {
    val ingredientState = viewModel.ingredientsState.collectAsState().value as IngredientState.Success
    val ingredientResponses = ingredientState.ingredients
    Box(
        modifier =
        modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            columns = StaggeredGridCells.Adaptive(200.dp)
        ) {
            items(count = ingredientResponses.size,
                key = { index -> index }
            ) { index ->
                IngredientCard(
                    ingredient = ingredientResponses[index].asDto(),
                    onIngredientClick = {
                        viewModel.updateIngredientInBasket(
                            ingredient = ingredientResponses[index]
                        )
                    },
                    isChecked = viewModel.isIngredientInBasket(
                        ingredient = ingredientResponses[index]
                    )
                )
            }
        }
    }
}
