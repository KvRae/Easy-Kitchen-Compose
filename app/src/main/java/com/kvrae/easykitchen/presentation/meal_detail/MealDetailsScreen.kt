package com.kvrae.easykitchen.presentation.meal_detail

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.data.remote.dto.MealDetail
import com.kvrae.easykitchen.presentation.meals.MealsViewModel
import com.kvrae.easykitchen.presentation.miscellaneous.components.CustomAlertDialogWithContent
import com.kvrae.easykitchen.utils.openYoutube
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsScreen(
    navController: NavController,
    meal : MealDetail,
    context: Context = LocalContext.current
) {
    val mealsViewModel = koinViewModel<MealsViewModel>()
    val savedMealIds by mealsViewModel.savedMealIds.collectAsState()
    val isFavorite = savedMealIds.contains(meal.id)

    var isButtonExtended by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    val scrollState = rememberScrollState()
    val imageHeight = 300f
    val imageAlpha by remember {
        derivedStateOf {
            when {
            scrollState.value <= 0 -> 1f
            scrollState.value < imageHeight -> 1f - (scrollState.value / imageHeight)
            else -> 0f
        }
        }

    }
    val favoriteIcon = if (!isFavorite) Icons.Rounded.FavoriteBorder else Icons.Rounded.Favorite

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.TopStart
        ) {
            SubcomposeAsyncImage(
                model = meal.image.orEmpty(),
                contentDescription = meal.name.orEmpty(),
                loading = {
                    CircularProgressIndicator()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .alpha(imageAlpha),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .padding(top = 250.dp)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                // Recipe Title
                Text(
                    text = meal.name.orEmpty(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                )

                // Info Pills Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoChip(
                        icon = Icons.Rounded.Place,
                        text = meal.area.orEmpty()
                    )
                    InfoChip(
                        icon = Icons.Rounded.Restaurant,
                        text = meal.category.orEmpty()
                    )
                    InfoChip(
                        icon = Icons.Rounded.Schedule,
                        text = "N/A"
                    )
                }

                // Ingredients & Instructions Section Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f))
                        .padding(20.dp)
                ) {
                    // Modern Tab Row with custom styling
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Ingredients Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (selectedTab == 0)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else
                                        Color.Transparent
                                )
                                .clickable { selectedTab = 0 }
                                .padding(horizontal = 12.dp, vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ingredients),
                                    contentDescription = "Ingredients",
                                    tint = if (selectedTab == 0)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.ingredients),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (selectedTab == 0)
                                        androidx.compose.ui.text.font.FontWeight.SemiBold
                                    else
                                        androidx.compose.ui.text.font.FontWeight.Normal,
                                    color = if (selectedTab == 0)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Instructions Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (selectedTab == 1)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else
                                        Color.Transparent
                                )
                                .clickable { selectedTab = 1 }
                                .padding(horizontal = 12.dp, vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.List,
                                    contentDescription = "Instructions",
                                    tint = if (selectedTab == 1)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.instructions),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (selectedTab == 1)
                                        androidx.compose.ui.text.font.FontWeight.SemiBold
                                    else
                                        androidx.compose.ui.text.font.FontWeight.Normal,
                                    color = if (selectedTab == 1)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tab Content with enhanced styling
                    if (selectedTab == 0) {
                        // Ingredients Tab
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .alignByBaseline(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                repeat(meal.ingredients.size) { index ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "•",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                            )
                                            Text(
                                                text = meal.ingredients[index],
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.65f)
                                    .alignByBaseline(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                repeat(meal.measures.size) { index ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = meal.measures[index],
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Instructions Tab
                        Text(
                            text = meal.instructions.orEmpty(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight.times(1.2f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
        if (!meal.youtube.isNullOrEmpty()){
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .height(56.dp)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxHeight(1f),
                    onClick = {
                        isButtonExtended= !isButtonExtended
                    },
                    content = {
                        Row {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ondemand_video_24
                                ),
                                contentDescription = "Youtube Icon",
                                tint = MaterialTheme.colorScheme.background,
                            )
                            AnimatedVisibility(
                                visible = isButtonExtended
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text="Available on Youtube ",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                )
            }
        }
        if (imageAlpha > 0f) {
            IconButton(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Arrow Back Button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            IconButton(
                modifier = Modifier
                    .statusBarsPadding()
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
                onClick = {
                    // Find the meal in the meals list to get the MealResponse
                    val mealResponse = mealsViewModel.findMealById(meal.id)
                    mealResponse?.let { mealsViewModel.toggleFavorite(it) }
                }
            ) {
                Icon(
                    imageVector = favoriteIcon,
                    contentDescription = stringResource(R.string.favorite_icon_description),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        if (imageAlpha <= 0f) {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = { Text(text = meal.name.orEmpty()) },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(8.dp),
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Favorite Icon Button",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                },
                actions = {
                    IconButton(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background),
                        onClick = {
                            // Find the meal in the meals list to get the MealResponse
                            val mealResponse = mealsViewModel.findMealById(meal.id)
                            mealResponse?.let { mealsViewModel.toggleFavorite(it) }
                        }
                    ) {
                        Icon(
                            imageVector = favoriteIcon,
                            contentDescription = "Favorite Icon Button",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            )
        }
        if (isButtonExtended){
            CustomAlertDialogWithContent(
                onDismiss = {isButtonExtended = false},
                title = "Youtube Video",
                confirmText = "Watch",
                dismissText = "Close",
                onConfirm = {
                    openYoutube(
                        uri = meal.youtube.orEmpty(),
                        context = context
                    )
                },
                content = {
                    Text(text = stringResource(R.string.watch_this_video_text))
                }
            )
        }
    }


}

@Composable
private fun InfoChip(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text.ifBlank { stringResource(id = R.string.not_available) },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
        )
    }
}
