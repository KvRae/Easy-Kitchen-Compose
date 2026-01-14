package com.kvrae.easykitchen.presentation.meal_detail

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.kvrae.easykitchen.presentation.miscellaneous.components.CustomAlertDialogWithContent
import com.kvrae.easykitchen.utils.openYoutube


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsScreen(
    navController: NavController,
    meal : MealDetail,
    context: Context = LocalContext.current
) {
    var isButtonExtended by remember { mutableStateOf(false) }

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
    var isFavorite by rememberSaveable {
        mutableStateOf(false)
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
                    .padding(24.dp)
            ) {
                // Recipe Title
                Text(
                    text = meal.name.orEmpty(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Info Pills Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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

                // Ingredients Section Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ingredients),
                            contentDescription = "Ingredients",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.ingredients),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(meal.ingredients.size) { index ->
                                Text(
                                    text = "• ${meal.ingredients[index]}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.weight(0.8f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(meal.measures.size) { index ->
                                Text(
                                    text = meal.measures[index],
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Instructions Section Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.List,
                            contentDescription = "Instructions",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.instructions),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Text(
                        text = meal.instructions.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )
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
            }}
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
                    isFavorite = !isFavorite
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
                            isFavorite = !isFavorite
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
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text.ifBlank { stringResource(id = R.string.not_available) },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
