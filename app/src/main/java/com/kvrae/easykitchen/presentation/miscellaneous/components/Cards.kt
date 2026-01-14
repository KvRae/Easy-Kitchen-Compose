package com.kvrae.easykitchen.presentation.miscellaneous.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.data.remote.dto.Category
import com.kvrae.easykitchen.data.remote.dto.Ingredient
import com.kvrae.easykitchen.data.remote.dto.Meal
import com.kvrae.easykitchen.data.remote.dto.MealDetail
import com.kvrae.easykitchen.utils.INGREDIENT_IMAGE_URL
import com.valentinilk.shimmer.shimmer

@Composable
fun MealCard(
    modifier: Modifier = Modifier,
    meal: Meal = Meal(),
    onMealClick: (String) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .width(240.dp)
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onMealClick(meal.id.orEmpty())
                }
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            SubcomposeAsyncImage(
                model = meal.image.orEmpty(),
                loading = {
                    Box {
                        MealShimmerCard()
                    }
                },
                contentDescription = stringResource(R.string.meal_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = meal.name.orEmpty(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = meal.area.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun MealShimmerCard(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .shimmer()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.onBackground)
    )
}

@Composable
fun CategoryCard(
    category: Category
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(50)),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(60))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(4.dp),

                ) {
                SubcomposeAsyncImage(
                    model = category.image.orEmpty(),
                    loading = {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp

                        )
                    },
                    contentDescription = stringResource(R.string.meal_image),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Text(
                text = category.name.orEmpty(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun MealByAreaAnsCategoryCard(
    modifier: Modifier = Modifier,
    meal: Meal = Meal(),
    onMealClick: (String) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onMealClick(meal.id.orEmpty())
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            SubcomposeAsyncImage(
                model = meal.image,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = stringResource(R.string.meal_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .height(64.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = meal.name.orEmpty(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = meal.category.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = meal.area.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun IngredientCard(
    modifier: Modifier = Modifier,
    onIngredientClick: (String) -> Unit,
    ingredient: Ingredient,
    isChecked: Boolean = false
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SubcomposeAsyncImage(
                model = ingredient.image,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = stringResource(R.string.meal_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .height(48.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(4.dp),

            )
            Text(
                text = ingredient.name.orEmpty(),
                style = MaterialTheme.typography.titleSmall,
            )

            IconToggleButton(
                checked = isChecked,
                onCheckedChange = {
                    onIngredientClick("Name")
                }
            ) {
                Icon(
                    imageVector = if (!isChecked) Icons.Rounded.Add else Icons.Rounded.Clear,
                    contentDescription = "Add/Remove Icon"
                )
            }
        }

    }
}


@Composable
fun MealImageCoveredCard(
    modifier: Modifier = Modifier,
    meal: MealDetail = MealDetail(),
    onMealClick: (String) -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: (String) -> Unit
) {
    val ingredinets = meal.ingredients
    Log.d("Ingredients List", ingredinets.size.toString())
    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onMealClick(meal.id.orEmpty()) }
    ) {

        SubcomposeAsyncImage(
            model = meal.image,
            loading = {
                MealCoveredImageShimmer()
            },
            contentDescription = stringResource(R.string.meal_image),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.7f))
                    )
                )
                .height(150.dp)
        ) {
            val favoriteIcon = if (isFavorite)
                Icons.Rounded.Favorite
            else Icons.Rounded.FavoriteBorder
            IconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(32.dp))
                    .size(46.dp)
                    .background(MaterialTheme.colorScheme.background),
                onClick = {
                    onFavoriteClick(meal.id.orEmpty())
                }
            ) {
                Icon(
                    imageVector = favoriteIcon,
                    contentDescription = "Add Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(bottom = 16.dp, start = 8.dp, end = 8.dp),
                text = meal.name.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                softWrap = true,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                    text = meal.area.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            if(ingredinets.size > 3)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, bottomStart = 32.dp))
                        .background(MaterialTheme.colorScheme.background)
                )
                {
                    Row {
                        repeat(3) {
                            Log.d("Ingredients List", ingredinets[it])
                            val name = ingredinets[it]
                            SubcomposeAsyncImage(
                                model = "$INGREDIENT_IMAGE_URL$name.png",
                                contentDescription = stringResource(R.string.meal_image),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .height(32.dp)
                                    .padding(4.dp),
                            )

                        }
                    }
                }
        }
    }
}

@Composable
fun MealCoveredImageShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .shimmer()
            .background(MaterialTheme.colorScheme.onBackground)

    )
}

@Composable
fun MealInfoCard(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Place,
    text: String = "Area"

) {
    Column(
        modifier = modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 8.dp)
            .width(70.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "an Icon",
            tint = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.padding(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}